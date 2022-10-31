package ar.edu.itba.paw.persistence;

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

public interface NewsDao {

    Page<News> getNews(int page, String query, NewsOrder ns, Long loggedUser);
    Page<News> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser);
    Page<News> getRecommendation(int page, User user, NewsOrder newsOrder);

    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id, Long loggedUser);
    void setRating(News news, User user, Rating rating);
    void saveNews(News news, User user);
    void removeSaved(News news, User user);
    void deleteNews(News news);
    Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory);
}
