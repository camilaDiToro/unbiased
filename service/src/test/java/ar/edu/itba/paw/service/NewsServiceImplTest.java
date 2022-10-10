package ar.edu.itba.paw.service;


import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.InvalidOrderException;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
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
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {

    // USER
    private static final String EMAIL = "user@mail.com";
    private static final User.UserBuilder USER_BUILDER = new User.UserBuilder(EMAIL);

    // NEWS
    private static final String TITTLE = "Title";
    private static final String SUBTITTLE = "Subtitle";
    private static final String BODY = "Body";
    private static final String INVALID_CATEGORY = "invalid.category";
    private static final String INVALID_NEWSORDER = "invalid.newsOrder";
    private static final long CREATOR_ID = 8;
    private static final News.NewsBuilder NEWS_BUILDER = new News.NewsBuilder(new User(USER_BUILDER),BODY,TITTLE,SUBTITTLE);

    // GET NEWS
    private static final int LOWER_PAGE = -5;
    private static final int TOTAL_PAGES = 5;
    private static final int UPPER_PAGE =  15;

    @Mock
    private UserDao mockUserDao;
    @Mock
    private NewsDao mockNewsDao;
    @Mock
    private UserService userService;
    @Mock
    private SecurityService mockSecurityService;

    @InjectMocks
    private NewsServiceImpl newsService;


    @Test(expected = InvalidCategoryException.class)
    public void testCreateInvalidCategory(){
        Mockito.when(userService.getUserById(Mockito.eq(CREATOR_ID))).thenReturn(Optional.of(USER_BUILDER.build()));
        Mockito.when(userService.getRoles(Mockito.any())).thenReturn(Collections.singletonList(Role.JOURNALIST));
        newsService.create(NEWS_BUILDER,new String[]{INVALID_CATEGORY});
        Assert.fail("Should have thrown InvalidCategoryException");
    }

    @Test
    public void testGetNewsLowerInvalidPage(){
        Mockito.when(mockSecurityService.getCurrentUser()).thenReturn(Optional.empty());
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.any())).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(LOWER_PAGE, "ALL", NewsOrder.NEW.getDescription(), "");
        assertEquals(1,returnValue.getCurrentPage());
    }

    @Test
    public void testGetNewsUpperInvalidPage(){
        Mockito.when(mockSecurityService.getCurrentUser()).thenReturn(Optional.empty());
        Mockito.when(mockNewsDao.getTotalPagesAllNews(Mockito.eq("A"))).thenReturn(TOTAL_PAGES);
        Page<News> returnValue = newsService.getNews(UPPER_PAGE, "ALL", NewsOrder.NEW.getDescription(), "A");
        assertEquals(TOTAL_PAGES,returnValue.getCurrentPage());
    }

    @Test(expected = InvalidOrderException.class)
    public void testGetNewsInvalidNewsOrder(){
        Mockito.when(mockSecurityService.getCurrentUser()).thenReturn(Optional.empty());
        newsService.getNews(UPPER_PAGE, "ALL", INVALID_NEWSORDER, "A");
        Assert.fail("Should have thrown InvalidOrderException");
    }


    @Test(expected = InvalidCategoryException.class)
    public void testGetNewsInvalidCategory() {
        Mockito.when(mockSecurityService.getCurrentUser()).thenReturn(Optional.empty());
        newsService.getNews(UPPER_PAGE, INVALID_CATEGORY, NewsOrder.NEW.getDescription(), "A");
        Assert.fail("Should have thrown InvalidOrderException");
    }

}
