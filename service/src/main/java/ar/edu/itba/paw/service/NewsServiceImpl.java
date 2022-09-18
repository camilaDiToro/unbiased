package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.persistence.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService{

    private final NewsDao newsDao;
    private final SecurityService securityService;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, SecurityService securityService) {
        this.newsDao = newsDao;
        this.securityService = securityService;
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
    public Page<News> getNewsFromUser(int page, String newsOrder, long userId) {
        page = page <= 0 ? 1 : page;
        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        int totalPages = newsDao.getTotalPagesNewsFromUser(page, userId, newsOrderObject);
        page = Math.min(page, totalPages);
        List<News> ln = newsDao.getAllNewsFromUser(page,userId,newsOrderObject);
        return new Page<>(ln, page, totalPages);
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        return newsDao.getNewsCategory(news);
    }

    @Override
    public void deleteNews(long newsId) {
        News news = newsDao.getById(newsId).orElseThrow(NewsNotFoundException::new);
        if(news.getCreatorId() != securityService.getCurrentUser().orElseThrow(() -> new HTTPException(400)).getId())
            throw new HTTPException(400);
        newsDao.deleteNews(newsId);
    }
}
