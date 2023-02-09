package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import java.util.List;

public interface AdminService {
    ReportDetail reportNews(long userId, long newsId, ReportReason reportReason);
    Page<News> getReportedNews(int page, ReportOrder reportOrder);
    List<News> getReportedByUserNews(User user);
    List<Comment> getReportedByUserComments(User user);
    ReportedComment reportComment(long userId, long commentId, ReportReason reportReason);
    Page<ReportDetail> getReportedNewsDetail(int page, long newsIs);
    void deleteNews(long newsId);
    void deleteComment(long commentId);
    Page<ReportedComment> getReportedCommentDetail(int page, long commentId);
}
