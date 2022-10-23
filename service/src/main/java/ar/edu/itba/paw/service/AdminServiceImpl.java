package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.AdminDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final NewsService newsService;
    private final SecurityService securityService;
    private final UserDao userDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, NewsService newsService, SecurityService securityService, UserDao userDao) {
        this.adminDao = adminDao;
        this.newsService = newsService;
        this.securityService = securityService;
        this.userDao = userDao;
    }
    private Long getLoggedUserId() {
        return securityService.getCurrentUser().map(User::getId).orElse(null);
    }

    @Override
    @Transactional
    public void reportNews(News news, ReportReason reportReason) {
        User user = securityService.getCurrentUser().get();
        adminDao.reportNews(news,user,reportReason);
    }

    @Override
    public Page<News> getReportedNews(int page, String reportOrder) {
       ReportOrder reportOrderObject = ReportOrder.getByValue(reportOrder);
       return adminDao.getReportedNews(page, reportOrderObject);
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, News news) {
        return adminDao.getReportedNewsDetail(page, news);
    }

    @Override
    public boolean hasReported(News news) {
        return adminDao.hasReported(news, getLoggedUserId());
    }

    @Override
    public void deleteNews(News news) {
        newsService.deleteNews(news);
    }
}
