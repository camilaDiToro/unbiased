package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

//    List<News> getNews(int page, NewsOrder ns, Long loggedUser);
    int getTotalPagesAllNews();
    List<FullNews> getNews(int page, String query, NewsOrder ns, Long loggedUser);
    int getTotalPagesAllNews(String query);
    News create(News.NewsBuilder newsBuilder);
    Optional<FullNews> getById(long id, Long loggedUser);
    List<FullNews> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser);
    int getTotalPagesCategory(Category category);
    List<Category> getNewsCategory(News news);

//    int getUpvotes(Long newsId);

//    Rating upvoteState(News news, User user);

    void setRating(News news,  User user, Rating rating);

//    NewsStats getNewsStats(Long newsId);

    List<FullNews> getSavedNews(int page, User user, NewsOrder ns, Long loggedUser);

    void saveNews(News news, User user);

//    boolean isSaved(News news, User user);

    void removeSaved(News news, User user);

    List<FullNews> getAllNewsFromUser(int page, User user, NewsOrder ns, Long loggedUser);
    int getTotalPagesNewsFromUser(int page, User user);

    int getTotalPagesNewsFromUserSaved(int page, User user);

    int getTotalPagesNewsFromUserUpvoted(int page, User user);

    int getTotalPagesNewsFromUserDownvoted(int page, User user);


    void deleteNews(News news);

    List<FullNews> getNewsUpvotedByUser(int page, User user, NewsOrder ns, Long loggedUser);

    List<FullNews> getNewsDownvotedByUser(int page, User user, NewsOrder ns, Long loggedUser);

//    List<News> getSavedNewsFromUser(int page, long userId, NewsOrder ns, Long loggedUser);


}
