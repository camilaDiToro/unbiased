package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {
    private static final String EMAIL = "user@mail.com";
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";

    static User.UserBuilder usBuilderTest = new User.UserBuilder(EMAIL);
    private static final User testUser = new User(usBuilderTest);
    static News.NewsBuilder nsBuilderTest = new  News.NewsBuilder(1, BODY, TITTLE, SUBTITTLE);
    private static final News testNews = new News(nsBuilderTest);

    @Mock
    private UserDao mockUserDao;
    @Mock
    private User mockUser;
    @Mock
    private NewsDao mockNewsDao;
    @Mock
    private News mockNews;
    @Mock
    private FullNews mockFullNews;
    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private NewsServiceImpl newsService;


    @Test
    public void testCreate() throws Exception {
        News.NewsBuilder newsBuilder = new News.NewsBuilder(testUser.getId(), BODY, TITTLE, SUBTITTLE);
        Mockito.when(mockNewsDao.create(Mockito.eq(newsBuilder))).thenReturn(testNews);
        News news = newsService.create(newsBuilder);

        Assert.assertNotNull(news);
        Assert.assertEquals(testNews.getTitle(), news.getTitle());
        Assert.assertEquals(testNews.getBody(), news.getBody());
        Mockito.verify(mockNewsDao).create(newsBuilder);
    }

    @Test
    public void testFindByNewsId() {
        News.NewsBuilder newsBuilder = new News.NewsBuilder(testUser.getId(), BODY, TITTLE, SUBTITTLE);
        Mockito.when(mockNewsDao.create(Mockito.eq(newsBuilder))).thenReturn(testNews);
        Mockito.when(mockNewsDao.getById(Mockito.eq(testNews.getNewsId()), null)).thenReturn(Optional.of(mockFullNews));
        News news = newsService.create(newsBuilder);

        Mockito.verify(mockNewsDao).create(newsBuilder);
        Assert.assertEquals(testNews.getNewsId(), news.getNewsId());
    }
}
