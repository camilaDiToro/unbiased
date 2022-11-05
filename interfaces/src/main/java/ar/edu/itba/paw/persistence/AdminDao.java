package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.*;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

public interface AdminDao {
    void reportNews(News news, User reporter, ReportReason reportReason);
    void reportComment(Comment comment, User reporter, ReportReason reportReason);
    Page<News> getReportedNews(int page, ReportOrder reportOrder);
    Page<Comment> getReportedComment(int page, ReportOrder reportOrder);
    Page<ReportDetail> getReportedNewsDetail(int page, News news);
    Page<ReportedComment> getReportedCommentDetail(int page, Comment comment);
    boolean hasReported(long newsId, Long loggedUser);
}
