package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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
    public News create(News.NewsBuilder newsBuilder) {
        return this.newsDao.create(newsBuilder);
    }

    @Override
    public Optional<News> getById(long id) {
        return newsDao.getById(id);
    }

    @Override
    public Page<News> getNews(int page, String category, String newsOrder, String query) {
        int totalPages;
        page = page <= 0 ? 1 : page;

        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        List<News> ln;
        if (category.equals("ALL")) {
            totalPages = newsDao.getTotalPagesAllNews(query);
            page = Math.min(page, totalPages);
            ln = newsDao.getNews(page, query, newsOrderObject);
        }
        else {
            Category catObject = Category.getByValue(category);
            totalPages = newsDao.getTotalPagesCategory(catObject);
            page = Math.min(page, totalPages);
            ln = newsDao.getNewsByCategory(page, catObject, newsOrderObject);
        }
        return new Page<>(ln,page,totalPages);
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
