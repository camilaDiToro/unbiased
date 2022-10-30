package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

public interface AdminService {
    void reportNews(News news, ReportReason reportReason);
    Page<News> getReportedNews(int page, ReportOrder reportOrder);
    void reportComment(Comment comment, ReportReason reportReason);
    Page<Comment> getReportedComments(int page, ReportOrder reportOrder);
    Page<ReportDetail> getReportedNewsDetail(int page, News news);
    void deleteNews(News news);
    void deleteComment(Comment comment);
    Page<ReportedComment> getReportedCommentDetail(int page, Comment comment);
    boolean hasReported(News news);
}
