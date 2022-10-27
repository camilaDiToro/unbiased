package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.AdminDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;
    private final NewsService newsService;
    private final SecurityService securityService;
    private final EmailService emailService;
    private final UserDao userDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, NewsService newsService, SecurityService securityService, EmailService emailService, UserDao userDao) {
        this.adminDao = adminDao;
        this.newsService = newsService;
        this.securityService = securityService;
        this.emailService = emailService;
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
    @Transactional
    public void reportComment(Comment comment, ReportReason reportReason) {
        User user = securityService.getCurrentUser().get();
        adminDao.reportComment(comment,user,reportReason);
    }

    @Override
    public Page<Comment> getReportedComments(int page, String reportOrder) {
        ReportOrder reportOrderObject = ReportOrder.getByValue(reportOrder);
        return adminDao.getReportedComment(page, reportOrderObject);
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, News news) {
        return adminDao.getReportedNewsDetail(page, news);
    }

    @Override
    public Page<ReportedComment> getReportedCommentDetail(int page, Comment comment) {
        return adminDao.getReportedCommentDetail(page, comment);
    }

    @Override
    public boolean hasReported(News news) {
        return adminDao.hasReported(news, getLoggedUserId());
    }

    @Override
    public void deleteNews(News news) {
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        emailService.sendNewsDeletedEmail(news.getCreator(), news, locale);
        newsService.deleteNews(news);
    }

    @Override
    public void deleteComment(Comment comment) {
        newsService.deleteComment(comment.getId());
    }

}
