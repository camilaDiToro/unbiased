package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.persistence.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService{

    private final NewsDao newsDao;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Override
    public List<News> getNews(int page) {
        return newsDao.getNews(page);
    }

    @Override
    public News create(News.NewsBuilder newsBuilder) {
        return this.newsDao.create(newsBuilder);
    }

    @Override
    public Optional<News> getById(long id) {
        return newsDao.getById(id);
    }

    @Override
    public int getTotalPagesAllNews() {
        return newsDao.getTotalPagesAllNews();
    }

    @Override
    public List<News> getNews(int page, String query) {
        return newsDao.getNews(page, query);
    }

    @Override
    public int getTotalPagesAllNews(String query) {
        return newsDao.getTotalPagesAllNews(query);
    }

    @Override
    public List<News> getNewsByCategory(int page, Category category) {
        return newsDao.getNewsByCategory(page,category);
    }

    @Override
    public int getTotalPagesCategory(int page, Category category) {
        return newsDao.getTotalPagesCategory(page,category);
    }
}
