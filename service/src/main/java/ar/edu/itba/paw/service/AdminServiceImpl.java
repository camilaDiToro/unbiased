package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
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
    public void reportNews(long newsId, ReportReason reportReason) {
        adminDao.reportNews(newsId,getLoggedUserId(),reportReason);
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page, NewsOrder ns) {
        return adminDao.getReportedNews(page, ns);
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, long newsId) {
        FullNews n = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        return adminDao.getReportedNewsDetail(page, newsId);
    }

    @Override
    public boolean hasReported(long newsId) {
        return adminDao.hasReported(newsId, getLoggedUserId());
    }

    @Override
    public void deleteNews(long newsId) {
        FullNews news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsService.deleteNews(news);
    }

    @Override
    public void makeUserAdmin(long userId) {
        User user = userService.getRegisteredUserById(userId).orElseThrow(UserNotFoundException::new);
        adminDao.makeUserAdmin(userId);
    }
}
