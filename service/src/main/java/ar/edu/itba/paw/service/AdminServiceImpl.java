package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AdminServiceImpl implements AdminService{

    private final CommentDao commentDao;
    private final NewsService newsService;
    private final SecurityService securityService;
    private final EmailService emailService;
    private final NewsDao newsDao;

    @Autowired
    public AdminServiceImpl(CommentDao commentDao, NewsService newsService, SecurityService securityService, EmailService emailService, NewsDao newsDao) {
        this.commentDao = commentDao;
        this.newsService = newsService;
        this.securityService = securityService;
        this.emailService = emailService;
        this.newsDao = newsDao;
    }
    private Long getLoggedUserId() {
        return securityService.getCurrentUser().map(User::getId).orElse(null);
    }

    @Override
    @Transactional
    public void reportNews(long newsId, ReportReason reportReason) {
        User user = securityService.getCurrentUser().get();
        News news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsDao.reportNews(news,user,reportReason);
    }

    @Override
    public Page<News> getReportedNews(int page, ReportOrder reportOrder) {
       return newsDao.getReportedNews(page, reportOrder);
    }

    @Override
    @Transactional
    public void reportComment(long commentId, ReportReason reportReason) {
        User user = securityService.getCurrentUser().get();
        Comment comment = newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new);
        commentDao.reportComment(comment,user,reportReason);
    }

    @Override
    public Page<Comment> getReportedComments(int page, ReportOrder reportOrder) {
        return commentDao.getReportedComment(page, reportOrder);
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, long newsId) {
        return newsDao.getReportedNewsDetail(page, newsId);
    }

    @Override
    public Page<ReportedComment> getReportedCommentDetail(int page, long commentId) {
        return commentDao.getReportedCommentDetail(page, commentId);
    }

    @Override
    public boolean hasReported(long newsId) {
        return newsDao.hasReported(newsId, getLoggedUserId());
    }

    @Override
    public void deleteNews(long newsId) {
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        News news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        emailService.sendNewsDeletedEmail(news.getCreator(), news, locale);
        newsService.deleteNews(news);
    }

    @Override
    public void deleteComment(long commentId) {
        newsService.deleteComment(commentId);
    }

}
