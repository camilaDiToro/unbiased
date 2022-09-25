package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.*;
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

    void setRating(Long newsId, Long userId, Rating rating);

    void addComment(User user, News news, String comment);

//    NewsStats getNewsStats(Long newsId);

    List<FullNews> getSavedNews(int page, long userId, NewsOrder ns, Long loggedUser);

    void saveNews(News news, User user);

//    boolean isSaved(News news, User user);

    void removeSaved(News news, User user);

    List<FullNews> getAllNewsFromUser(int page, long userId, NewsOrder ns, Long loggedUser);
    int getTotalPagesNewsFromUser(int page, long userId);

    int getTotalPagesNewsFromUserSaved(int page, long userId);

    int getTotalPagesNewsFromUserUpvoted(int page, long userId);

    int getTotalPagesNewsFromUserDownvoted(int page, long userId);


    void deleteNews(long newsId);

    List<FullNews> getNewsUpvotedByUser(int page, long userId, NewsOrder ns, Long loggedUser);

    List<FullNews> getNewsDownvotedByUser(int page, long userId, NewsOrder ns, Long loggedUser);

    Page<Comment> getComments(long newsId, int page);

//    List<News> getSavedNewsFromUser(int page, long userId, NewsOrder ns, Long loggedUser);


}
