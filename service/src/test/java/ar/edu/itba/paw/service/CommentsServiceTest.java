package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.NewsDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CommentsServiceTest {

    private static final Category CATEGORY = Category.ALL;
    private static final NewsOrder ORDER = NewsOrder.TOP;
    private static final TimeConstraint TIME = TimeConstraint.ALLTIME;
    private static final ReportOrder REPORT_ORDER = ReportOrder.REP_COUNT_DESC;
    // GET NEWS
    private static final int LOWER_PAGE = -5;
    private static final int TOTAL_PAGES = 5;
    private static final int UPPER_PAGE =  15;

    private static final String QUERY = "a";

    private static final Optional<User> EMPTY_USER = Optional.empty();

    private static final long NEWS_ID = 1;

    private static final int PAGE_NUM = 1;

    private static final long ID_1 = 1;

    private static final long ID_2 = 2;


    @Mock
    private UserService userService;

    @Mock
    private NewsDao mockNewsDao;

    @Mock
    private CommentDao mockCommentDao;

    @Mock
    private News NEWS_1;

    @Mock
    private News NEWS_2;

    @Mock
    private Comment COMMENT_1;

    @Mock
    private Comment COMMENT_2;

    @Mock
    private User mockUser;

    @Mock
    private Page<Comment> COMMENT_PAGE_2;


    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    public void testGetComments(){
        Mockito.when(mockCommentDao.getTopComments(Mockito.eq(NEWS_ID), Mockito.eq(PAGE_NUM))).thenReturn(COMMENT_PAGE_2);

        assertEquals(COMMENT_PAGE_2,commentService.getComments(NEWS_ID, PAGE_NUM, ORDER, false, REPORT_ORDER));
    }

    @Test
    public void testGetReportedComments(){
        Mockito.when(mockCommentDao.getReportedComment(Mockito.eq(PAGE_NUM), Mockito.eq(REPORT_ORDER))).thenReturn(COMMENT_PAGE_2);

        assertEquals(COMMENT_PAGE_2,commentService.getComments(NEWS_ID, PAGE_NUM, ORDER, true, REPORT_ORDER));
    }

    @Test
    public void testGetTopComments(){
        Mockito.when(mockCommentDao.getNewComments(Mockito.eq(NEWS_ID), Mockito.eq(PAGE_NUM))).thenReturn(COMMENT_PAGE_2);

        assertEquals(COMMENT_PAGE_2,commentService.getComments(NEWS_ID, PAGE_NUM, NewsOrder.NEW, false, REPORT_ORDER));
    }


    @Test
    public void testsetCommentRating(){
        Map<Long, CommentUpvote> upvoteMap = new HashMap<>();
        Mockito.when(COMMENT_1.getUpvoteMap()).thenReturn(upvoteMap);
        Mockito.when(mockUser.getId()).thenReturn(ID_1);
        Mockito.when(mockCommentDao.getCommentById(COMMENT_1.getId())).thenReturn(Optional.of(COMMENT_1));
        assertTrue(commentService.setCommentRating(ID_1, COMMENT_1.getId(),Rating.UPVOTE));
        assertTrue(upvoteMap.get(ID_1).isValue());
    }

    @Test
    public void testsetCommentRatingRemove(){
        Map<Long, CommentUpvote> upvoteMap = new HashMap<>();
        upvoteMap.put(ID_1, new CommentUpvote(COMMENT_1, ID_1, true));
        Mockito.when(COMMENT_1.getUpvoteMap()).thenReturn(upvoteMap);
        Mockito.when(mockUser.getId()).thenReturn(ID_1);
        Mockito.when(mockCommentDao.getCommentById(COMMENT_1.getId())).thenReturn(Optional.of(COMMENT_1));
        assertTrue(commentService.setCommentRating(ID_1, COMMENT_1.getId(),Rating.NO_RATING));
        assertEquals(0, upvoteMap.size());
    }


}

