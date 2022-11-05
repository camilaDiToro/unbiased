package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;


import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder, String[] categories);
    Optional<News> getById(long id);
    Optional<Comment> getCommentById(long id);
    Page<News> getNews(int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query);
    void setRating(News news, Rating rating);
    boolean toggleSaveNews(long newsId);
    void deleteNews(News news);
    Iterable<ProfileCategory> getProfileCategories(User user);
    Iterable<Category> getHomeCategories();
    Page<News> getNewsForUserProfile(int page, NewsOrder newsOrder, User user, String pc);
    void addComment(long newsId, String comment);
    Page<Comment> getComments(long newsId, int page);
    void deleteComment(long commentId);
}
