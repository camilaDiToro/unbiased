package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
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

    private String getUrl(String subdir){
        try {
            return new URL(environment.getRequiredProperty("mail.url.schema"), environment.getRequiredProperty("mail.url.domain"), environment.getRequiredProperty("mail.url.baseDir") + subdir).toString();
        } catch (MalformedURLException e) {
            LOGGER.warn("Malformed url exeption in email verification {}", e);
        }
        return null;
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
            LOGGER.info("Verification email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Verification email could not be sent to {}",user.getEmail());
        }
    }

    @Async
    @Override
    public void sendAdminEmail(User user, Locale locale) {
        final String to = user.getEmail();
        final String subject = messageSource.getMessage("email.admin.subject",null,locale);
        Map<String, Object> data = new HashMap<>();
        final String url = getUrl("admin/reported_news/REP_COUNT_DESC");
        data.put("username", user.getUsername());
        data.put("urlToAdminPanel",url);
        try {
            sendMessageUsingThymeleafTemplate(to,subject,"new-admin.html",data,locale);
            LOGGER.info("Admin email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("Admin email could not be sent to {}",user.getEmail());
        }
    }

    @Async
    @Override
    public void sendNewsDeletedEmail(User user, News news, Locale locale) {
        final String to = user.getEmail();
        final String subject = messageSource.getMessage("email.newsDeleted.subject",null,locale);
        final String url = getUrl("create_article");
        Map<String, Object> data = new HashMap<>();
        data.put("urlToCreateArticle",url);
        try {
            sendMessageUsingThymeleafTemplate(to,subject,"news-deleted.html",data,locale);
            LOGGER.info("News deleted email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("News deleted could not be sent to {}",user.getEmail());
        }
    }

    @Async
    @Override
    public void sendNewFollowerEmail(User user, User follower, Locale locale) {
        final String to = user.getEmail();
        final String subject = messageSource.getMessage("email.newFollower.subject",null,locale);
        final String url = getUrl("create_article");
        Map<String, Object> data = new HashMap<>();
        data.put("urlToCreateArticle",url);
        try {
            sendMessageUsingThymeleafTemplate(to,subject,"new-follower.html",data,locale);
            LOGGER.info("New follower email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.warn("New follower could not be sent to {}",user.getEmail());
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

    private void sendMessageUsingThymeleafTemplate(String to, String subject, String template, Map<String, Object> templateModel, Locale locale) throws MessagingException {
        Context thymeleafContext = new Context(locale);
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
        sendHtmlMessage(to, subject, htmlBody);
    }
}