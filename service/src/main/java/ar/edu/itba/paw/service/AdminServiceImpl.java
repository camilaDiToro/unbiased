package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.InvalidOrderException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final NewsService newsService;
    private final UserService userService;
    private final SecurityService securityService;


    @Autowired
    public AdminServiceImpl(AdminDao adminDao, NewsService newsService, UserService userService, SecurityService securityService) {
        this.adminDao = adminDao;
        this.newsService = newsService;
        this.userService = userService;
        this.securityService = securityService;
    }
    private Long getLoggedUserId() {
        return securityService.getCurrentUser().map(User::getId).orElse(null);
    }


    @Override
    public void reportNews(News news, ReportReason reportReason) {
        adminDao.reportNews(news,getLoggedUserId(),reportReason);
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page, String reportOrder) {
       ReportOrder reportOrderObject;

        try {
            reportOrderObject = ReportOrder.valueOf(reportOrder);
        }
        catch (Exception e) {
            throw new InvalidOrderException();
        }
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

    @Override
    public void makeUserAdmin(User user) {
        adminDao.makeUserAdmin(user);
    }
}
