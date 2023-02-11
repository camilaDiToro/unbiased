package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import java.util.List;
import java.util.Optional;

public interface NewsDao {
    List<News> getNewNews(int page, String query, long loggedUser);
    List<News> getNewNews(int page, String query);
    List<News> getTopNews(int page, String query, TimeConstraint timeConstraint, long loggedUser);
    Optional<News> getById(long id);
    List<News> getNewsByCategoryNew(int page, Category category, long loggedUser);
    int getTotalPagesCategoryNew(Category category);
    List<News> getRecommendationNew(int page, User user);
    List<News> getRecommendationTop(int page, User user, TimeConstraint timeConstraint);
    int getRecommendationNewsPageCountNew(User user);
    News create(News.NewsBuilder newsBuilder);
    List<News> getTopNews(int page, String query, TimeConstraint timeConstraint);
    int getTotalPagesAllNews(String query, TimeConstraint timeConstraint);
    Optional<News> getById(long id, long loggedUser);
    int getTotalPagesCategoryTop(Category category, TimeConstraint timeConstraint);
    void saveNews(News news, User user);
    void removeSaved(News news, User user);
    List<News> getNewsByCategoryNew(int page, Category category);
    List<News> getNewsByCategoryTop(int page, Category category, long loggedUser, TimeConstraint timeConstraint);
    List<News> getNewsByCategoryTop(int page, Category category, TimeConstraint timeConstraint);
    void deleteNews(News news);
    Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, Optional<User> loggedUser, ProfileCategory profileCategory);
    int getRecommendationNewsPageCountTop(User user, TimeConstraint timeConstraint);
    CategoryStatistics getCategoryStatistics(long userId);
    List<News> getReportedByUserNews(long userId);
    Optional<News> getPinnedByUserNews(long userId);
    ReportDetail reportNews(News news, User reporter, ReportReason reportReason);
    Page<News> getReportedNews(int page, ReportOrder reportOrder);
    Page<ReportDetail> getReportedNewsDetail(int page, long newsId);
    boolean isReportedByUser(final News news,final User user);
    boolean isSavedByUser(long newsId, long userId);
}
