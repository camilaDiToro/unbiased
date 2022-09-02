package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class NewsJdbcDaoTest {
    private static final String NEWS_TABLE = "news";

    private static final String EMAIL = "juan@gmail.com";
    private static final long ID= 1;
    private static final String TITTLE = "titulo";
    private static final String SUBTITTLE = "subtitulo";
    private static final String BODY = "cuerpo";
    private static final int PAGE_SIZE = 1;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private NewsJdbcDao newsDao;
    @Autowired
    private DataSource ds;

    News.NewsBuilder nwBuilder = new News.NewsBuilder(ID, BODY, TITTLE, SUBTITTLE);

    @Before
    public void setUp() {
        newsDao = new NewsJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateNews(){
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        News news = newsDao.create(nwBuilder);

        // 3. validaciones
        assertNotNull(news);
        assertEquals(TITTLE, news.getTitle());
        assertEquals(BODY, news.getBody());
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,NEWS_TABLE));
    }

    @Test
    public void testGetNewsExists(){
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        News news = newsDao.create(nwBuilder);
        News news2 = newsDao.create(nwBuilder);
        News news3 = newsDao.create(nwBuilder);
        List<News> newsList = newsDao.getNews(PAGE_SIZE);

        // 3. validaciones
        assertEquals(3, newsList.size());
    }

    @Test
    public void testGetTotalPageNews(){
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        News news = newsDao.create(nwBuilder);

        // 3. validaciones
        assertEquals(PAGE_SIZE, newsDao.getTotalPagesAllNews());
    }

    @Test
    public void testSearchNews(){}

    @Test
    public void testSearchByCategory(){}
}
