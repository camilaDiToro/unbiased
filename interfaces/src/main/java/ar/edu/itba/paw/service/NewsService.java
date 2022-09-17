package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;
import ar.edu.itba.paw.model.Page;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
    Page<News> getNews(int page, String category, String newsOrder, String query);
    Page<News> getNewsFromUser(int page, String newsOrder, long userId);
    List<Category> getNewsCategory(News news);
}
