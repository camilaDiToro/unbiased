package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import java.util.Locale;

public interface EmailService {
    void sendVerificationEmail(User user, VerificationToken token, Locale locale);
    void sendAdminEmail(User user, Locale locale);
    void sendNewsDeletedEmail(User user, News news, Locale locale);
    void sendNewFollowerEmail(User user, User follower, Locale locale);
}
