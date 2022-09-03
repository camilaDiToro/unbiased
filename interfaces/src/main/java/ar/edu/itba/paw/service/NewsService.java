package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;

import java.util.List;
import java.util.Optional;

public interface NewsService {

    List<News> getNews(int page, NewsOrder ns);
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
    int getTotalPagesAllNews();
   List<News> getNews(int page, String query, NewsOrder ns);
    int getTotalPagesAllNews(String query);
    List<News> getNewsByCategory(int page, Category category, NewsOrder ns);
    int getTotalPagesCategory(Category category);
}
