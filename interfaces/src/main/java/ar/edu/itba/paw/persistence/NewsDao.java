package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

    List<FullNews> getNews(int page, String query, NewsOrder ns, Long loggedUser);
    int getTotalPagesAllNews(String query);
    List<FullNews> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser);
    int getTotalPagesCategory(Category category);
    List<FullNews> getRecommendation(int page, User user, NewsOrder newsOrder);
    int getTodayNewsPageCount();

    int getTotalPagesAllNews();
    News create(News.NewsBuilder newsBuilder);
    Optional<FullNews> getById(long id, Long loggedUser);
    Optional<News> getSimpleNewsById(long id);
    List<Category> getNewsCategory(News news);
    void setRating(Long newsId, Long userId, Rating rating);
    void addComment(User user, News news, String comment);
    void saveNews(News news, User user);
    void removeSaved(News news, User user);
    void deleteNews(News news);
    Page<Comment> getComments(long newsId, int page);
    Page<FullNews> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory);
}
