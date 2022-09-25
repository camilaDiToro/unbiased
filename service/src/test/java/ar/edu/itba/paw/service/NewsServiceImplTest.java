package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {
    private static final String EMAIL = "user@mail.com";
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";

    @Mock
    private User mockUser;
    @Mock
    private NewsDao mockNewsDao;
    @Mock
    private News mockNews;
    @Mock
    private FullNews mockFullNews;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Before
    public void setTest() {
        mockUser = new User.UserBuilder(EMAIL).build();
        mockNews = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE).build();
    }

    @Test
    public void testCreate() throws Exception {
        News.NewsBuilder newsBuilder = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE);
        Mockito.when(mockNewsDao.create(Mockito.eq(newsBuilder))).thenReturn(mockNews);

        News news = newsService.create(newsBuilder);

        Assert.assertNotNull(news);
        Assert.assertEquals(mockNews.getTitle(), news.getTitle());
        Assert.assertEquals(mockNews.getBody(), news.getBody());
        Mockito.verify(mockNewsDao).create(newsBuilder);
    }

    @Test
    public void testFindByNewsId() {
        News.NewsBuilder newsBuilder = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE);
        Mockito.when(mockNewsDao.create(Mockito.eq(newsBuilder))).thenReturn(mockNews);
        Mockito.when(mockNewsDao.getById(Mockito.eq(mockNews.getNewsId()), null)).thenReturn(Optional.of(mockFullNews));

        Optional<FullNews> news = newsService.getById(mockNews.getNewsId());

        Assert.assertEquals(mockFullNews.getNews().getNewsId(), news.get().getNews().getNewsId());
    }
}
