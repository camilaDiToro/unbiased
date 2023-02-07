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

import java.util.List;
import java.util.Locale;

@Service
public class AdminServiceImpl implements AdminService{

    private final CommentDao commentDao;
    private final NewsService newsService;
    private final EmailService emailService;
    private final NewsDao newsDao;

    @Autowired
    public AdminServiceImpl(final CommentDao commentDao, final NewsService newsService, final SecurityService securityService, final EmailService emailService, final NewsDao newsDao) {
        this.commentDao = commentDao;
        this.newsService = newsService;
        this.emailService = emailService;
        this.newsDao = newsDao;
    }

    @Override
    @Transactional
    public void reportNews(final User currentUser, long newsId, ReportReason reportReason) {
        final News news = newsService.getById(currentUser, newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));
        newsDao.reportNews(news,currentUser,reportReason);
    }

    @Override
    public Page<News> getReportedNews(int page, ReportOrder reportOrder) {
       return newsDao.getReportedNews(page, reportOrder);
    }

    @Override
    public List<News> getReportedByUserNews(User user) {
        return newsDao.getReportedByUserNews(user.getUserId());
    }

    @Override
    public List<Comment> getReportedByUserComments(User user) {
        return commentDao.getReportedByUserComments(user.getUserId());
    }

    @Override
    @Transactional
    public void reportComment(final User currentUser, long commentId, ReportReason reportReason) {
        final Comment comment = newsService.getCommentById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId));
        commentDao.reportComment(comment,currentUser,reportReason);
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
    public void deleteNews(long newsId) {

        final News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));
        User creator = news.getCreator();
        final Locale locale = creator.getEmailSettings() != null ? creator.getEmailSettings().getLocale() :  LocaleContextHolder.getLocale();
        emailService.sendNewsDeletedEmail(creator, news, locale);
        newsService.deleteNews(news);
    }

    @Override
    public void deleteComment(long commentId) {
        newsService.deleteComment(commentId);
    }

}
