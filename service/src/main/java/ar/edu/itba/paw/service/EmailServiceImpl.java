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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Environment environment;

    private final MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Async
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
    @Override
    public void sendVerificationEmail(User user, VerificationToken token, Locale locale) {
        final String to = user.getEmail();
        final String text = messageSource.getMessage("email.verification.text",null,locale) +" "+ getUrl("verifyEmail?token="+token.getToken());
        final String subject = messageSource.getMessage("email.verification.subject",null,locale);
        sendSimpleMessage(to, subject, text);
    }
}