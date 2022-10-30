package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;

public interface AdminService {
    void reportNews(long newsId, ReportReason reportReason);
    Page<News> getReportedNews(int page, ReportOrder reportOrder);
    void reportComment(long commentId, ReportReason reportReason);
    Page<Comment> getReportedComments(int page, ReportOrder reportOrder);
    Page<ReportDetail> getReportedNewsDetail(int page, long newsIs);
    void deleteNews(long newsId);
    void deleteComment(long commentId);
    Page<ReportedComment> getReportedCommentDetail(int page, long commentId);
    boolean hasReported(long newsId);
}
