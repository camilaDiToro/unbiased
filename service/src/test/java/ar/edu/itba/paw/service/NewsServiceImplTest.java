package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.Upvote;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {

    private static final Category CATEGORY = Category.ALL;
    private static final NewsOrder ORDER = NewsOrder.NEW;
    private static final TimeConstraint TIME = TimeConstraint.ALLTIME;

    // GET NEWS
    private static final int LOWER_PAGE = -5;
    private static final int TOTAL_PAGES = 5;
    private static final int UPPER_PAGE =  15;

    private static final String QUERY = "a";

    private static final Optional<User> EMPTY_USER = Optional.empty();

    private static final long NEWS_ID = 1;

    private static final long ID_1 = 1;

    private static final long ID_2 = 2;


    @Mock
    private UserService userService;

    @Mock
    private NewsDao mockNewsDao;

    @Mock
    private News NEWS_1;

    @Mock
    private Comment COMMENT_1;

    @Mock
    private Comment COMMENT_2;

    @Mock
    private User mockUser;


    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    public void testGetNewsLowerInvalidPage(){
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(EMPTY_USER,LOWER_PAGE, CATEGORY, ORDER,TIME, QUERY);
        assertEquals(1,returnValue.getCurrentPage());
    }

    @Test
    public void testGetNewsUpperInvalidPage(){
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(EMPTY_USER,UPPER_PAGE, CATEGORY, ORDER,TIME, QUERY);
        assertEquals(TOTAL_PAGES,returnValue.getCurrentPage());
    }

    @Test
    public void testGetNewsQueryMatch(){
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.matches(QUERY), Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(EMPTY_USER,1, CATEGORY, ORDER,TIME, QUERY);
        assertEquals(TOTAL_PAGES,returnValue.getTotalPages());
    }

    @Test
    public void testGetComentsRating(){
        List<Comment> commentList = Arrays.asList(COMMENT_1, COMMENT_2);
        Mockito.when(COMMENT_1.getId()).thenReturn(ID_1);
        Mockito.when(COMMENT_2.getId()).thenReturn(ID_2);

        Map<Long, Rating> ratingMap = newsService.getCommentsRating(commentList, EMPTY_USER);
        ratingMap.values().forEach(r -> assertEquals(Rating.NO_RATING, r));
    }

    @Test
    public void testGetHomeCategoriesUser(){

        for (Category cat : newsService.getHomeCategories(EMPTY_USER)) {
            assertNotEquals(Category.FOR_ME,cat);
        }
    }

    @Test
    public void testGetProfileCategoriesUser(){
        Mockito.when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.ROLE_JOURNALIST));
        for (ProfileCategory cat : newsService.getProfileCategories(EMPTY_USER, mockUser)) {
            assertNotEquals(ProfileCategory.SAVED,cat);
        }
    }

    @Test
    public void testsetRating(){
        Map<Long, Upvote> upvoteMap = new HashMap<>();
        Mockito.when(NEWS_1.getUpvoteMap()).thenReturn(upvoteMap);
        Mockito.when(mockUser.getId()).thenReturn(ID_1);
        Mockito.when(NEWS_1.getPositivityStats()).thenReturn(new PositivityStats(0,0));
        Mockito.when(userService.getUserById(ID_1)).thenReturn(Optional.of(mockUser));
        Mockito.when(mockNewsDao.getById(NEWS_ID)).thenReturn(Optional.of(NEWS_1));
        newsService.setRating(mockUser.getId(), NEWS_ID,Rating.UPVOTE);
        assertTrue(upvoteMap.get(ID_1).isValue());
    }

    @Test
    public void testsetRatingRemove(){
        Map<Long, Upvote> upvoteMap = new HashMap<>();
        upvoteMap.put(ID_1, new Upvote(NEWS_1, ID_1, true));
        Mockito.when(NEWS_1.getUpvoteMap()).thenReturn(upvoteMap);
        Mockito.when(mockUser.getId()).thenReturn(ID_1);
        Mockito.when(NEWS_1.getPositivityStats()).thenReturn(new PositivityStats(0,0));
        Mockito.when(userService.getUserById(ID_1)).thenReturn(Optional.of(mockUser));
        Mockito.when(mockNewsDao.getById(NEWS_ID)).thenReturn(Optional.of(NEWS_1));
        newsService.setRating(mockUser.getId(), NEWS_ID,Rating.NO_RATING);
        assertEquals(0, upvoteMap.size());
    }

}

