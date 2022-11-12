package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.*;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

public interface AdminDao {


    void reportComment(Comment comment, User reporter, ReportReason reportReason);
    Page<Comment> getReportedComment(int page, ReportOrder reportOrder);
    Page<ReportedComment> getReportedCommentDetail(int page, Comment comment);

}
