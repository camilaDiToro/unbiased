package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.CommentUpvote;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {


    private final CommentDao commentDao;


    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {

        this.commentDao = commentDao;
    }

    @Override
    public List<Comment> getCommentsUpvotedByUser(User user) {
        return commentDao.getCommentsUpvotedByUser(user);
    }

    @Override
    public List<Comment> getCommentsDownvotedByUser(User user) {
        return commentDao.getCommentsDownvotedByUser(user);
    }

    @Override
    public Optional<Comment> getById(long id) {
        return commentDao.getCommentById(id);
    }

    @Override
    @Transactional
    public void setCommentRating(final User currentUser, Comment comment, Rating rating) {
        Map<Long, CommentUpvote> upvoteMap = comment.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
            upvoteMap.remove(currentUser.getId());
            return;
        }
        final long userId = currentUser.getId();

        upvoteMap.putIfAbsent(userId, new CommentUpvote(comment, currentUser.getId()));
        upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));
    }

}
