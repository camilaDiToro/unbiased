package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.jws.soap.SOAPBinding;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {
    private static final String EMAIL = "user@mail.com";
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";

    @Mock
    private User mockUser;
    @Mock
    private UserDao mockUserDao;
    @Mock
    private NewsDao mockNewsDao;
    @Mock
    private News mockNews;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Before
    public void setTest() {
        mockUser = new User.UserBuilder(EMAIL).build();
        mockNews = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE).newsId(8).build();
    }

    @Test
    public void testCreate(){
        News.NewsBuilder newsBuilder = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE);
        News mnews = new News(newsBuilder);
        String[] categories = new String[0];
        Mockito.when(mockNewsDao.create(Mockito.any())).thenReturn(mnews);

        News news = newsService.create(newsBuilder, categories);

        Assert.assertEquals(mockNews.getTitle(), news.getTitle());
        Assert.assertEquals(mockNews.getBody(), news.getBody());
        //Mockito.verify(mockNewsDao).create(newsBuilder);
    }

    @Test
    public void testGetNewsForUserProfile(){
        User.UserBuilder USER_BUILDER = new User.UserBuilder(EMAIL);
        User us = mockUserDao.create(USER_BUILDER);
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(mockUser);

        News.NewsBuilder NEWS_BUILDER = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE);
        News mnews = new News(NEWS_BUILDER);

        String[] categories = new String[0];
        Mockito.when(mockNewsDao.create(Mockito.eq(NEWS_BUILDER))).thenReturn(mnews);

        News news = newsService.create(NEWS_BUILDER, categories);
        User user = mockUserDao.getUserById(news.getCreatorId()).get();
        Page<FullNews> fullNewsPage = newsService.getNewsForUserProfile(1, "NEW", user, "myPosts");

        Assert.assertEquals(1, fullNewsPage.getTotalPages());
        //Mockito.verify(mockNewsDao).create(newsBuilder);
    }
}
