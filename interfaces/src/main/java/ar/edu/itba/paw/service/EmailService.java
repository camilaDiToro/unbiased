package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.VerificationToken;
import java.util.Locale;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendVerificationEmail(User user, VerificationToken token, Locale locale);
}
