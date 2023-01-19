package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder, final List<Category> categories);
    Optional<News> getById(Optional<User> maybeCurrentUser, long id);
    Optional<News> getById(final User currentUser, long id);
    Optional<News> getById(long id);
    Optional<Comment> getCommentById(long id);
    Page<News> getNews(Optional<User> maybeCurrentUser, int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query);
    void setRating(final User currentUser, News news, Rating rating);
    void setCommentRating(final User currentUser, Comment comment, Rating rating);


    void saveNews(User currentUser, long newsId);


    void unsaveNews(User currentUser, long newsId);

    boolean toggleSaveNews(final User currentUser, long newsId);
    void deleteNews(News news);
    Iterable<ProfileCategory> getProfileCategories(Optional<User> maybeCurrentUser, final User user);
    Iterable<Category> getHomeCategories(Optional<User> maybeCurrentUser);
    Optional<News> getPingedNews(Optional<User> maybeCurrentUser, final  User profileUser);
    Page<News> getNewsForUserProfile(Optional<User> maybeCurrentUser, int page, NewsOrder newsOrder, final User user, ProfileCategory pc);
    Map<Long, Rating> getCommentsRating(List<Comment> comments, Optional<User> maybeLoggedUser);
    Comment addComment(final User currentUser, long newsId, String comment);
    Page<Comment> getComments(long newsId, int page, NewsOrder orderByObj, Boolean reported, ReportOrder reportOrder);
    void deleteComment(long commentId);
    CategoryStatistics getCategoryStatistics(final long userId);
}
