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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final NewsService newsService;
    private final UserService userService;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, NewsService newsService, UserService userService) {
        this.adminDao = adminDao;
        this.newsService = newsService;
        this.userService = userService;
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
        News n = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        return adminDao.getReportedNewsDetail(page, newsId);
    }

    @Override
    public void deleteNews(long newsId) {
        News news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsService.deleteNews(newsId);
    }

    @Override
    public void makeUserAdmin(long userId) {
        User user = userService.getRegisteredUserById(userId).orElseThrow(UserNotFoundException::new);
        adminDao.makeUserAdmin(userId);
    }
}
