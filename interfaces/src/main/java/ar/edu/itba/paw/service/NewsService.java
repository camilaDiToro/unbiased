package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
    Page<FullNews> getNews(int page, String category, String newsOrder, String query);

    Page<FullNews> getNewsFromUser(int page, String newsOrder, long userId);

    List<Category> getNewsCategory(News news);

    int getUpvotes(Long newsId);

    Rating upvoteState(News news, User user);

    void setRating(Long newsId, Long userId, Rating rating);

    NewsStats getNewsStats(Long newsId);

//    Positivity getPositivityBracket(Long newsId);

    List<FullNews> getSavedNews(int page, User user, NewsOrder ns);

    boolean toggleSaveNews(News news, User user);

    Optional<FullNews> getFullNewsById(long id);

    boolean isSaved(News news, User user);

    void deleteNews(long newsId);

    Page<FullNews> getNewsForUserProfile(int page, String newsOrder, long userId, String pc);
}
