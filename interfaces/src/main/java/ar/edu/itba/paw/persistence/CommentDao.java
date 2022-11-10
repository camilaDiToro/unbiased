package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import java.util.Optional;

public interface CommentDao {
    Optional<Comment> getCommentById(long id);
    void addComment(User user, News news, String comment);
    void deleteComment(long commentId);
    Page<Comment> getComments(long newsId, int page);
}
