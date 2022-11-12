package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class NewsJpaDaoTest {
    //USERS DATA
    private static final String EMAIL = "user@gmail.com";
    private static final String PASS = "pass";
    private static final long CREATOR_ID = 1;
    private static final UserStatus CREATOR_STATUS = UserStatus.REGISTERED;
    //NEWS DATA
    private static final long NEWS_ID = 1;
    private static final String TITLE = "titulo";
    private static final String SUBTITLE = "subtitulo";
    private static final String BODY = "cuerpo";
    private static final int PAGE_SIZE = 1;
    private static final Category CATEGORY = Category.SPORTS;
    private static final Timestamp CREATION_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static final long ACCESSES = 0;
    private static final User CREATOR = new User.UserBuilder(EMAIL).pass(PASS).build();
    private static final News NEWS = new News.NewsBuilder(CREATOR,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(NEWS_ID).build();

    //TABLES
    private static final String NEWS_TABLE = "news";
    private static final String USER_TABLE = "users";
    private static final String CATEGORY_TABLE = "news_category";
    private static final String SAVED_TABLE = "saved_news";
    private static final String UPVOTES_TABLE = "upvotes";

    @Autowired
    private NewsJpaDao newsJpaDao;
    @Autowired
    private UserJpaDao userJpaDao;
    @Autowired
    private DataSource ds;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcNewsInsert;

    private void addCreatorToTable() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("pass", PASS);
        userValues.put("status", CREATOR_STATUS.getStatus());
        userValues.put("user_id", CREATOR_ID);
        jdbcUserInsert.execute(userValues);
    }

    private void addTheNewsToTable() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("accesses", ACCESSES);
        newsValues.put("creator", CREATOR);
        newsValues.put("body", BODY);
        newsValues.put("creation_date", CREATION_DATE);
        newsValues.put("title", TITLE);
        newsValues.put("subtitle", SUBTITLE);
        newsValues.put("creator", CREATOR);
        jdbcNewsInsert.execute(newsValues);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
        jdbcNewsInsert = new SimpleJdbcInsert(ds).withTableName(NEWS_TABLE);
    }

    @Test
    public void testCreateNews() {
        addCreatorToTable();
        User user = userJpaDao.getUserById(CREATOR_ID).get();
        News.NewsBuilder newsBuilder = new News.NewsBuilder(user,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(NEWS_ID);

        News optionalNews = newsJpaDao.create(newsBuilder);


        assertEquals(TITLE, optionalNews.getTitle());
        assertEquals(CREATOR_ID, optionalNews.getCreatorId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    /*@Test
    public void testGetNewsById() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional <News> optionalNews = newsDao.getById(NEWS_ID, CREATOR_ID);

        assertEquals(NEWS_ID, optionalNews.get().getNewsId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }
*/
}
