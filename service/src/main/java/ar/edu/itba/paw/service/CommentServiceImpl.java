package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.NewsOrder;
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
    private final UserService userService;


    @Autowired
    public CommentServiceImpl(CommentDao commentDao, UserService userService) {
        this.commentDao = commentDao;
        this.userService = userService;
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
    public boolean setCommentRating(long userId, long commentId, Rating rating) {
        Comment comment = commentDao.getCommentById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId));

        Map<Long, CommentUpvote> upvoteMap = comment.getUpvoteMap();

        if(!upvoteMap.containsKey(userId)){
            if(rating.equals(Rating.NO_RATING)){
                return false;
            }
            upvoteMap.put(userId, new CommentUpvote(comment, userId));
            upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));
            return true;
        }
        if(rating.equals(Rating.NO_RATING)){
            upvoteMap.remove(userId);
            return true;
        }
        if(upvoteMap.get(userId).isValue()){
            if(rating.equals(Rating.UPVOTE)){
                return false;
            }
            upvoteMap.get(userId).setValue(false);
            return true;
        }

        if(rating.equals(Rating.DOWNVOTE)){
            return false;
        }
        upvoteMap.get(userId).setValue(true);
        return true;
    }

    @Override
    @Transactional
    public Page<Comment> getComments(long newsId, int page, NewsOrder orderByObj, final boolean reported, ReportOrder reportOrder) {
        if(!reported){
            if (orderByObj.equals(NewsOrder.NEW)) {
                return commentDao.getNewComments(newsId, page);
            }
            return commentDao.getTopComments(newsId, page);
        }

        return commentDao.getReportedComment(page, reportOrder);
    }

}
