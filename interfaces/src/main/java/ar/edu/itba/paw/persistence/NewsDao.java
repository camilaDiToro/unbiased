package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;

import java.util.List;
import java.util.Optional;

public interface NewsDao {

    List<News> getNews(int page);
    int getTotalPagesAllNews();
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
}
