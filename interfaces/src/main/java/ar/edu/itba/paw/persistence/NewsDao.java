package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;

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

    List<News> getAllNewsFromUser(int page, long userId, NewsOrder ns);
    int getTotalPagesNewsFromUser(int page, long userId, NewsOrder ns);
    void deleteNews(long newsId);
}
