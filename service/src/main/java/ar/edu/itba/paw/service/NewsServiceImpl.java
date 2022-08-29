package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.persistence.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService{

    private final NewsDao newsDao;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Override
    public News create(News.NewsBuilder newsBuilder) {
        return this.newsDao.create(newsBuilder);
    }

    @Override
    public Optional<News> getById(long id) {
        return newsDao.getById(id);
    }
}
