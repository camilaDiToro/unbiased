package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;


import java.util.List;
import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder, String[] categories);
    Optional<News> getById(long id);
    Optional<Comment> getCommentById(long id);
    Page<News> getNews(int page, String category, String newsOrder, String query);
    List<Category> getNewsCategory(News news);
    void setRating(News news, Rating rating);

    boolean toggleSaveNews(News news, User user);
    void deleteNews(News news);
    Iterable<ProfileCategory> getProfileCategories(User user);
    Iterable<Category> getHomeCategories();
    Page<News> getNewsForUserProfile(int page, String newsOrder, User user, String pc);
    Page<News> getRecommendation(int page, User user, NewsOrder newsOrder);
    News getOrThrowException(long newsId);
    void addComment(News news, String comment);
    Page<Comment> getComments(News news, int page);
    void deleteComment(long commentId);
    NewsOrder getOrderBy();
}
