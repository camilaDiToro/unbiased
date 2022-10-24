package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import java.util.Locale;

public interface EmailService {
    void sendVerificationEmail(User user, VerificationToken token, Locale locale);
    void sendAdminEmail(User user, Locale locale);
}
