package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
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
    public List<News> getNews(int page, NewsOrder newsOrder) {
        return newsDao.getNews(page, newsOrder);
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
    public List<News> getNews(int page, String query, NewsOrder newsOrder) {
        return newsDao.getNews(page, query, newsOrder);
    }

    @Override
    public int getTotalPagesAllNews(String query) {
        return newsDao.getTotalPagesAllNews(query);
    }

    @Override
    public List<News> getNewsByCategory(int page, Category category, NewsOrder newsOrder) {
        return newsDao.getNewsByCategory(page,category,newsOrder);
    }

    @Override
    public int getTotalPagesCategory(Category category) {
        return newsDao.getTotalPagesCategory(category);
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        return newsDao.getNewsCategory(news);
    }

    @Override
    public int getUpvotes(Long newsId) {
        return newsDao.getUpvotes(newsId);
    }
    @Override

    public Rating upvoteState(News news, User user) {
        return newsDao.upvoteState(news, user);
    }

    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {
        newsDao.setRating(newsId, userId, rating);
    }
    @Override
    public double getPositivityValue(Long newsId) {
        return newsDao.getPositivityValue(newsId);
    }

    @Override
    public Positivity getPositivityBracket(Long newsId) {
        return Positivity.getPositivvity(getPositivityValue(newsId));
    }


}
