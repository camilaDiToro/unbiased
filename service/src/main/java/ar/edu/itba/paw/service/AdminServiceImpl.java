package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.AdminDao;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final NewsDao newsDao;
    private final UserDao userDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, NewsDao newsDao, UserDao userDao) {
        this.adminDao = adminDao;
        this.newsDao = newsDao;
        this.userDao = userDao;
    }

    @Override
    public void reportNews(long newsId, long userId, ReportReason reportReason) {
        adminDao.reportNews(newsId,userId,reportReason);
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page) {
        return adminDao.getReportedNews(page);
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, long newsId) {
        News n = newsDao.getById(newsId).orElseThrow(NewsNotFoundException::new);
        return adminDao.getReportedNewsDetail(page, newsId);
    }

    @Override
    public void deleteNews(long newsId) {
        News news = newsDao.getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsDao.deleteNews(newsId);
    }

    @Override
    public void makeUserAdmin(long userId) {
        User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);
        adminDao.makeUserAdmin(userId);
    }
}
