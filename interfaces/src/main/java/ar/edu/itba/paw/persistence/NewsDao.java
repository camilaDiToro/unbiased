package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import java.util.List;
import java.util.Optional;

public interface NewsDao {

    List<News> getNewNews(int page, String query, Long loggedUser);

    List<News> getTopNews(int page, String query, TimeConstraint timeConstraint, Long loggedUser);

    int getTotalPagesAllNews(String query);
    List<News> getNewsByCategoryNew(int page, Category category, Long loggedUser);
    int getTotalPagesCategoryNew(Category category);
    List<News> getRecommendationNew(int page, User user);

    List<News> getRecommendationTop(int page, User user, TimeConstraint timeConstraint);

    int getRecommendationNewsPageCountNew(User user);

    News create(News.NewsBuilder newsBuilder);

    int getTotalPagesAllNews(String query, TimeConstraint timeConstraint);

    Optional<News> getById(long id, Long loggedUser);
    Optional<Comment> getCommentById(long id);

    int getTotalPagesCategoryTop(Category category, TimeConstraint timeConstraint);

    void addComment(User user, News news, String comment);
    void deleteComment(long commentId);
    void saveNews(News news, User user);
    void removeSaved(News news, User user);

    List<News> getNewsByCategoryTop(int page, Category category, Long loggedUser, TimeConstraint timeConstraint);

    void deleteNews(News news);
    Page<Comment> getComments(long newsId, int page);
    Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory);

    int getRecommendationNewsPageCountTop(User user, TimeConstraint timeConstraint);
}
