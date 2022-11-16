package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.InvalidOrderException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {

    private static final Category CATEGORY = Category.ALL;
    private static final NewsOrder NEWS_ORDER = NewsOrder.NEW;
    private static final TimeConstraint TIME = TimeConstraint.ALLTIME;

    // GET NEWS
    private static final int LOWER_PAGE = -5;
    private static final int TOTAL_PAGES = 5;
    private static final int UPPER_PAGE =  15;

    private static final String QUERY = "a";

    private static final String OTHER_QUERY = "b";

    private static final Optional<User> EMPTY_USER = Optional.empty();

    @Mock
    private NewsDao mockNewsDao;


    @InjectMocks
    private NewsServiceImpl newsService;

    @Test
    public void testGetNewsLowerInvalidPage(){
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(EMPTY_USER,LOWER_PAGE, CATEGORY, NEWS_ORDER,TIME, QUERY);
        assertEquals(1,returnValue.getCurrentPage());
    }

    @Test
    public void testGetNewsUpperInvalidPage(){
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.any(), Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(EMPTY_USER,UPPER_PAGE, CATEGORY, NEWS_ORDER,TIME, QUERY);
        assertEquals(TOTAL_PAGES,returnValue.getCurrentPage());
    }

    @Test
    public void testGetNewsQueryMatch(){
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.matches(QUERY), Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(EMPTY_USER,1, CATEGORY, NEWS_ORDER,TIME, QUERY);
        assertEquals(TOTAL_PAGES,returnValue.getTotalPages());
    }

    //getComments

    //getCommentsRating

    //getHomeCategories

    //getProfileCategories

    //setRating

    //setCommentRating


}

