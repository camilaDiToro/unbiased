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
    void sendNewCommentEmail(User newsOwner, News commentedNews, Locale locale);
    void sendNewPublishedNewsByFollowing(User user, News publishedNews, Locale locale);
    void sendNewsPositivityChanged(User newsOwner, News news, Locale locale);
}