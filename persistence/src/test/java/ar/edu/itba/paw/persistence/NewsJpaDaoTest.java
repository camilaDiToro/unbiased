package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class NewsJpaDaoTest {
    //USERS DATA
    private static final String EMAIL = "user@gmail.com";
    private static final String EMAIL2 = "user2@gmail.com";
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
    private static final User CREATOR2 = new User.UserBuilder(EMAIL2).pass(PASS).build();
    private static final News NEWS = new News.NewsBuilder(CREATOR,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(NEWS_ID).build();
    private static final News.NewsBuilder NEWS_BUILDER = new News.NewsBuilder(CREATOR,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(NEWS_ID);
    //REPORT DATA
    private static final ReportReason REPORT_REASON= ReportReason.LIE;

    //TABLES
    private static final String NEWS_TABLE = "news";
    private static final String USER_TABLE = "users";
    private static final String CATEGORY_TABLE = "news_category";
    private static final String REPORT_TABLE = "report";

    @Autowired
    private NewsJpaDao newsJpaDao;
    @Autowired
    private DataSource ds;

    @PersistenceContext
    EntityManager entityManager;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcNewsInsert;
    private SimpleJdbcInsert jdbcCategoryInsert;
    private SimpleJdbcInsert jdbcReportInsert;

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
        newsValues.put("body", BODY);
        newsValues.put("creation_date", CREATION_DATE);
        newsValues.put("title", TITLE);
        newsValues.put("creator", CREATOR_ID);
        newsValues.put("subtitle", SUBTITLE);
        jdbcNewsInsert.execute(newsValues);
    }

    private void addCategoryToTheNews() {
        Map<String, Object> categoryValues = new HashMap<>();
        categoryValues.put("category_id", CATEGORY.getId());
        categoryValues.put("news_id", NEWS_ID);
        jdbcCategoryInsert.execute(categoryValues);
    }

    private void addReportToTheNews() {
        Map<String, Object> reportValues = new HashMap<>();
        reportValues.put("creation_date", CREATION_DATE);
        reportValues.put("news_id", NEWS_ID);
        reportValues.put("creator", CREATOR_ID);
        reportValues.put("reason", REPORT_REASON.getDescription());
        jdbcCategoryInsert.execute(reportValues);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
        jdbcNewsInsert = new SimpleJdbcInsert(ds).withTableName(NEWS_TABLE);
        jdbcCategoryInsert = new SimpleJdbcInsert(ds).withTableName(CATEGORY_TABLE);
        jdbcReportInsert = new SimpleJdbcInsert(ds).withTableName(REPORT_TABLE);
    }

    @Test
    public void testCreateNews() {
        addCreatorToTable();
        addTheNewsToTable();
        News optionalNews = newsJpaDao.create(NEWS_BUILDER);

        assertEquals(TITLE, optionalNews.getTitle());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetNewsById() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional<News> optionalNews = newsJpaDao.getById(NEWS_ID, CREATOR_ID);

        assertEquals(NEWS_ID, optionalNews.get().getNewsId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByCreatorId() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional<News> optionalFullNews = newsJpaDao.getById(NEWS_ID, CREATOR_ID);

        assertEquals(optionalFullNews.get().getCreatorId(), NEWS_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testDeleteNews() {
        addCreatorToTable();
        addTheNewsToTable();

        newsJpaDao.deleteNews(NEWS);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetNewsByCategory(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();

        List<News> newsList = newsJpaDao.getNewsByCategoryNew(PAGE_SIZE, CATEGORY, CREATOR_ID);

        assertEquals(1, newsList.size());
        //assertEquals(newsList.get(0).getCategories(), CATEGORY);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CATEGORY_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetNewsByCategoryTop(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();

        List<News> newsList = newsJpaDao.getNewsByCategoryTop(PAGE_SIZE, CATEGORY, CREATOR_ID, TimeConstraint.DAY);

        assertEquals(1, newsList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CATEGORY_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    /*@Test
    public void testReportNews(){
        addCreatorToTable();
        addTheNewsToTable();
        addReportToTheNews();

        newsJpaDao.reportNews(new News(NEWS_BUILDER), CREATOR, REPORT_REASON);
        //entityManager.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REPORT_TABLE));
    }*/

    @Test
    public void testGetReportNewsDetail(){
        newsJpaDao.reportNews(NEWS, CREATOR, REPORT_REASON);
        Page<ReportDetail> reportPage = newsJpaDao.getReportedNewsDetail(PAGE_SIZE, NEWS.getNewsId());
        entityManager.flush();

        assertEquals(REPORT_REASON, reportPage.getContent().get(0).getReason());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REPORT_TABLE));
    }

}
