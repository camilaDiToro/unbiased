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

    List<News> getNews(int page, String query, NewsOrder ns, Long loggedUser);
    int getTotalPagesAllNews(String query);
    List<News> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser);
    int getTotalPagesCategory(Category category);
    List<News> getRecommendation(int page, User user, NewsOrder newsOrder);
    int getTodayNewsPageCount(User user);

    News create(News.NewsBuilder newsBuilder);

    Optional<News> getById(long id, Long loggedUser);
    Optional<Comment> getCommentById(long id);
    List<Category> getNewsCategory(News news);
    void setRating(News news, User user, Rating rating);
    void addComment(User user, News news, String comment);
    void deleteComment(long commentId);
    void saveNews(News news, User user);
    void removeSaved(News news, User user);
    void deleteNews(News news);
    Page<Comment> getComments(long newsId, int page);
    Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory);
}
