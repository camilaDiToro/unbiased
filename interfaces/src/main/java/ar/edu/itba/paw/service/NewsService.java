package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
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
    News create(News.NewsBuilder newsBuilder, List<Category> categories);
    Optional<News> getById(Optional<User> maybeCurrentUser, long id);
    Optional<News> getById(User currentUser, long id);
    Optional<News> getById(long id);
    Optional<Comment> getCommentById(long id);
    Page<News> getNews(Optional<User> maybeCurrentUser, int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query);
    void setRating(User currentUser, News news, Rating rating);
    void setCommentRating(User currentUser, Comment comment, Rating rating);
    boolean toggleSaveNews(User currentUser, long newsId);
    void deleteNews(News news);
    Iterable<ProfileCategory> getProfileCategories(Optional<User> maybeCurrentUser, User user);
    Iterable<Category> getHomeCategories(Optional<User> maybeCurrentUser);

    Optional<News> getPingedNews(Optional<User> maybeCurrentUser, User profileUser);

    Page<News> getNewsForUserProfile(Optional<User> maybeCurrentUser, int page, NewsOrder newsOrder, User user, ProfileCategory pc);
    Map<Long, Rating> getCommentsRating(List<Comment> comments, Optional<User> maybeLoggedUser);
    void addComment(User currentUser, long newsId, String comment);
    Page<Comment> getComments(long newsId, int page, NewsOrder orderByObj);
    void deleteComment(long commentId);
    CategoryStatistics getCategoryStatistics(final long userId);
}
