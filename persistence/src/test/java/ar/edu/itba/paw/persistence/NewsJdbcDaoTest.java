package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class NewsJdbcDaoTest {
    private NewsJdbcDao newsDao;
    private UserJdbcDao userDao;
    @Autowired
    private DataSource ds;
    private CategoryDao categoryDao;
    protected JdbcTemplate jdbcTemplate;


    protected static final String NEWS_TABLE = "news";

    //USERS DATA
    protected static final String EMAIL = "juan@gmail.com";

    //NEWS DATA
    protected static final long CREATOR_ID = 1;
    protected static final long IMAGEN_ID = 1;
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";
    protected static final int PAGE_SIZE = 1;
    protected static LocalDateTime CREATE_TIME = LocalDateTime.now();

    private User getMockUser() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        User user = userDao.createIfNotExists(usBuilder);
        return user;
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        newsDao = new NewsJdbcDao(ds, categoryDao);
        userDao = new UserJdbcDao(ds);
        categoryDao = new CategoryJdbcDao(ds);
        //insertNews(CREATOR_ID, IMAGEN_ID, BODY, TITTLE, SUBTITTLE, CREATE_TIME);
    }

    @Test
    public void testCreateNews() {
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        //Optional<User> mayBeUser = userDao.getUserById(user.getId());
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        // 3. validaciones
        assertNotNull(user);
        assertNotNull(news);
        assertEquals(TITTLE, news.getTitle());
        assertEquals(BODY, news.getBody());
    }

    @Test
    public void testFindById() {
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        Optional<News> optionalNews = newsDao.getById(news.getNewsId());
        assertNotNull(optionalNews);
        if (optionalNews.isPresent())
            assertEquals(news.getNewsId(), optionalNews.get().getNewsId());
    }

    @Test
    public void testFindByIdFailure() {
        Optional<News> news = newsDao.getById(80);
        assertFalse(news.isPresent());
    }

    @Test
    public void testGetNewsInCategoryNew(){
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        assertNotNull(news);

        List<News> newsList = newsDao.getNews(PAGE_SIZE, NewsOrder.NEW);

        // 3. validaciones
        assertEquals(PAGE_SIZE, newsList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));

    }



    @Test
    public void testGetTotalPageNews(){
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        // 3. validaciones
        assertEquals(PAGE_SIZE, newsDao.getTotalPagesAllNews());
    }

    @Test
    public void testSearchNews(){}

}
