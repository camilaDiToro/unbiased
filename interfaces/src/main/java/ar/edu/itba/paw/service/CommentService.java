package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentService {
    List<Comment> getCommentsUpvotedByUser(User user);
    List<Comment> getCommentsDownvotedByUser(User user);
    Optional<Comment> getById(long id);
    void setCommentRating(User currentUser, Comment comment, Rating rating);
}
