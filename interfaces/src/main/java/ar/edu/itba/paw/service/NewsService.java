package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.News;

import java.util.List;
import java.util.Optional;

public interface NewsService {

    List<News> getNews(int page);
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
    int getTotalPagesAllNews();

   List<News> getNews(int page, String query);

    int getTotalPagesAllNews(String query);
}
