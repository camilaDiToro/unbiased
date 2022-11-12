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
    private final EmailService emailService;
    private final NewsDao newsDao;

    @Autowired
    public AdminServiceImpl(CommentDao commentDao, NewsService newsService, EmailService emailService, NewsDao newsDao) {
        this.commentDao = commentDao;
        this.newsService = newsService;
        this.emailService = emailService;
        this.newsDao = newsDao;
    }

    @Override
    @Transactional
    public void reportNews(User currentUser, long newsId, ReportReason reportReason) {
        News news = newsService.getById(currentUser, newsId).orElseThrow(NewsNotFoundException::new);
        newsDao.reportNews(news,currentUser,reportReason);
    }

    @Override
    public Page<News> getReportedNews(int page, ReportOrder reportOrder) {
       return newsDao.getReportedNews(page, reportOrder);
    }

    @Override
    @Transactional
    public void reportComment(User currentUser, long commentId, ReportReason reportReason) {
        Comment comment = newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new);
        commentDao.reportComment(comment,currentUser,reportReason);
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
    public boolean hasReported(long userId, long newsId) {
        return newsDao.hasReported(newsId, userId);
    }

    @Override
    public void deleteNews(User currentUser, long newsId) {
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        News news = newsService.getById(currentUser, newsId).orElseThrow(NewsNotFoundException::new);
        emailService.sendNewsDeletedEmail(news.getCreator(), news, locale);
        newsService.deleteNews(news);
    }

    @Override
    public void deleteComment(long commentId) {
        newsService.deleteComment(commentId);
    }

}
