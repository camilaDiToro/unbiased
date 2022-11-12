package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import java.util.Optional;

public interface CommentDao {
    Optional<Comment> getCommentById(long id);
    void addComment(User user, News news, String comment);
    void deleteComment(long commentId);
    Page<Comment> getTopComments(long newsId, int page);
    Page<Comment> getNewComments(long newsId, int page);
    void reportComment(Comment comment, User reporter, ReportReason reportReason);
    Page<Comment> getReportedComment(int page, ReportOrder reportOrder);
    Page<ReportedComment> getReportedCommentDetail(int page, long commentId);
}
