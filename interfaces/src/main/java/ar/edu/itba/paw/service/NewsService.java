package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.News;

import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
}
