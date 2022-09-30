package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

    int getTotalPagesAllNews();
    List<FullNews> getNews(int page, String query, NewsOrder ns, Long loggedUser);
    int getTotalPagesAllNews(String query);
    News create(News.NewsBuilder newsBuilder);
    Optional<FullNews> getById(long id, Long loggedUser);
    Optional<News> getSimpleNewsById(long id);
    List<FullNews> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser);
    int getTotalPagesCategory(Category category);
    List<Category> getNewsCategory(News news);
    void setRating(Long newsId, Long userId, Rating rating);
    void addComment(User user, News news, String comment);
    List<FullNews> getSavedNews(int page, long userId, NewsOrder ns, Long loggedUser);
    void saveNews(News news, User user);
    void removeSaved(News news, User user);
    List<FullNews> getAllNewsFromUser(int page, User user, NewsOrder ns, Long loggedUser);
    int getTotalPagesNewsFromUser(int page, User user);
    int getTotalPagesNewsFromUserSaved(int page, User user);
    int getTotalPagesNewsFromUserUpvoted(int page, User user);
    int getTotalPagesNewsFromUserDownvoted(int page, User user);
    void deleteNews(News news);
    List<FullNews> getNewsUpvotedByUser(int page, User user, NewsOrder ns, Long loggedUser);
    List<FullNews> getNewsDownvotedByUser(int page, User user, NewsOrder ns, Long loggedUser);
    List<FullNews> getRecommendation(int page, User user, NewsOrder newsOrder);
    int getTodayNewsPageCount();
    Page<Comment> getComments(long newsId, int page);
}
