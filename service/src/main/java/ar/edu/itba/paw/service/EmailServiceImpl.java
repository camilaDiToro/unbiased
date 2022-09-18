package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Environment environment;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;


    private final MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private String getUrl(String subdir){
        try {
            return new URL(environment.getRequiredProperty("mail.url.schema"), environment.getRequiredProperty("mail.url.domain"), environment.getRequiredProperty("mail.url.baseDir") + subdir).toString();
        } catch (MalformedURLException e) {
            LOGGER.warn("Malformed url exeption in email verification");
        }
        return null;
    }

    @Async
    //@Override
    public void sendVerificationEmailPlain(User user, VerificationToken token, Locale locale) {
        final String to = user.getEmail();
        final String text = messageSource.getMessage("email.verification.text",null,locale) +" "+ getUrl("verify_email?token="+token.getToken());
        final String subject = messageSource.getMessage("email.verification.subject",null,locale);
        sendSimpleMessage(to, subject, text);
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, VerificationToken token, Locale locale) {
        final String to = user.getEmail();
        final String url = getUrl("verify_email?token=" + token.getToken());
        final String subject = messageSource.getMessage("email.verification.subject",null,locale);
        Map<String, Object> data = new HashMap<>();
        data.put("verificationUrl",url);
        try {
            sendMessageUsingThymeleafTemplate(to,subject,"verify-email.html",data,locale);
        } catch (MessagingException e) {
            LOGGER.warn("The verification email for " + to + "could not be sent");
        }
    }

    /* https://www.baeldung.com/spring-email-templates */
    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

    public void sendMessageUsingThymeleafTemplate(String to, String subject, String template, Map<String, Object> templateModel, Locale locale) throws MessagingException {
        Context thymeleafContext = new Context(locale);
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
        sendHtmlMessage(to, subject, htmlBody);
    }
}