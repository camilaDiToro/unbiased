package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder);
    Optional<FullNews> getById(long id);
    Page<FullNews> getNews(int page, String category, String newsOrder, String query);
    List<Category> getNewsCategory(News news);
    void setRating(News news, Rating rating);
    boolean toggleSaveNews(FullNews news, User user);
    void deleteNews(News news);
    Page<FullNews> getNewsForUserProfile(int page, String newsOrder, User user, String pc);
    Page<FullNews> getRecommendation(int page, User user);
    Optional<News> getSimpleNewsById(long id);
    FullNews getOrThrowException(long newsId);
    void addComment(News news, String comment);
    Page<Comment> getComments(News news, int page);
}
