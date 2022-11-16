package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import java.util.Locale;

public interface EmailService {
    void sendVerificationEmail(final User user, final VerificationToken token, Locale locale);
    void sendAdminEmail(final User user, Locale locale);
    void sendNewsDeletedEmail(final User user, final News news, Locale locale);
    void sendNewFollowerEmail(final User user,final  User follower, Locale locale);
    void sendNewCommentEmail(final User newsOwner, final News commentedNews, Locale locale);
    void sendNewPublishedNewsByFollowing(final User user, final News publishedNews, Locale locale);
    void sendNewsPositivityChanged(final User newsOwner, final News news, Locale locale);
}