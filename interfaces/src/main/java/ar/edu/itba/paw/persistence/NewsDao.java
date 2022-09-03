package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

    List<News> getNews(int page);
    int getTotalPagesAllNews();
    List<News> getNews(int page, String query);
    int getTotalPagesAllNews(String query);
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
    List<News> getNewsByCategory(int page, Category category);
    int getTotalPagesCategory(int page, Category category);
}
