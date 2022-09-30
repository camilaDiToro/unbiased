package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
        String[] categories = new String[0];
        Mockito.when(mockNewsDao.create(Mockito.eq(newsBuilder))).thenReturn(mockNews);

        News news = newsService.create(newsBuilder, categories);

        Assert.assertNotNull(news);
        Assert.assertEquals(mockNews.getTitle(), news.getTitle());
        Assert.assertEquals(mockNews.getBody(), news.getBody());
        Mockito.verify(mockNewsDao).create(newsBuilder);
    }

}
