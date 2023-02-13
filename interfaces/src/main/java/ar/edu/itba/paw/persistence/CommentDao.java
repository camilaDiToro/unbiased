package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    Optional<Comment> getCommentById(long id);
    Comment addComment(final User user,final News news, String comment);
    void deleteComment(long commentId);
    Page<Comment> getTopComments(long newsId, int page);
    Page<Comment> getNewComments(long newsId, int page);
    Page<Comment> getReportedByUserComments(int page, long userId);
    ReportedComment reportComment(final Comment comment, final User reporter, ReportReason reportReason);
    Page<Comment> getReportedComment(int page, ReportOrder reportOrder);
    Page<ReportedComment> getReportedCommentDetail(int page, long commentId);
    List<Comment> getCommentsUpvotedByUser(User user);
    List<Comment> getCommentsDownvotedByUser(User user);
    boolean isReportedByUser(long commentId, long userId);
}
