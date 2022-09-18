package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

    List<News> getNews(int page, NewsOrder ns);
    int getTotalPagesAllNews();
    List<News> getNews(int page, String query, NewsOrder ns);
    int getTotalPagesAllNews(String query);
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
    List<News> getNewsByCategory(int page, Category category, NewsOrder ns);
    int getTotalPagesCategory(Category category);
    List<Category> getNewsCategory(News news);

    int getUpvotes(Long newsId);

    Rating upvoteState(News news, User user);

    void setRating(Long newsId, Long userId, Rating rating);

    double getPositivityValue(Long newsId);

    List<News> getSavedNews(int page, User user, NewsOrder ns);

    void saveNews(News news, User user);

    boolean isSaved(News news, User user);

    void removeSaved(News news, User user);

    List<News> getAllNewsFromUser(int page, long userId, NewsOrder ns);
    int getTotalPagesNewsFromUser(int page, long userId, NewsOrder ns);
    void deleteNews(long newsId);

    List<News> getNewsUpvotedByUser(int page, long userId, NewsOrder ns);

    List<News> getNewsDownvotedByUser(int page, long userId, NewsOrder ns);

    List<News> getSavedNewsFromUser(int page, long userId, NewsOrder ns);

}
