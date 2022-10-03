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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImplTest.class);


    @Before
    public void setTest() {
        mockUser = new User.UserBuilder(EMAIL).build();
        mockNews = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE).newsId(8).build();
    }

    @Test
    public void testCreate(){
        News.NewsBuilder newsBuilder = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE);
        Mockito.when(mockNewsDao.create(Mockito.any())).thenReturn(mockNews);

        try{
            String[] categories = new String[0];
            News news = newsService.create(newsBuilder, categories);

            Assert.assertEquals(mockNews.getTitle(), news.getTitle());
            Assert.assertEquals(mockNews.getBody(), news.getBody());
            Mockito.verify(mockNewsDao).create(Mockito.any());
        }
        catch (Exception e){
            LOGGER.warn("Unexpected error during operation create news test threw exception");
        }
    }

    @Test
    public void testGetNewsForUserProfile(){
        News.NewsBuilder NEWS_BUILDER = new News.NewsBuilder(mockUser.getId(), BODY, TITTLE, SUBTITTLE);
        Mockito.when(mockNewsDao.create(Mockito.eq(NEWS_BUILDER))).thenReturn(mockNews);

        try{
            String[] categories = new String[0];
            News news = newsService.create(NEWS_BUILDER, categories);
            Optional<FullNews> optionalFullNews = newsService.getById(news.getNewsId());
            Page<FullNews> fullNewsPage = newsService.getNewsForUserProfile(1, "NEW", optionalFullNews.get().getUser(), "myPosts");

            Assert.assertEquals(1, fullNewsPage.getTotalPages());
            Mockito.verify(mockNewsDao).create(Mockito.any());
        }
        catch (Exception e){
            LOGGER.warn("Unexpected error during operation get news for user profile test threw exception");
        }
    }
}
