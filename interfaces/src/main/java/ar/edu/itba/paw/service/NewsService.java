package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.News;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder);
}
