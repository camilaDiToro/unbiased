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
    private static final String PASS = "pass";
    private static final long CREATOR_ID = 1;
    private static final UserStatus CREATOR_STATUS = UserStatus.REGISTERED;
    //NEWS DATA
    private static final long NEWS_ID = 1;
    private static final long NEWS_ID2 = 2;
    private static final String TITLE = "titulo";
    private static final String SUBTITLE = "subtitulo";
    private static final String BODY = "cuerpo";
    private static final int PAGE_SIZE = 1;
    private static final Category CATEGORY = Category.SPORTS;
    private static final Timestamp CREATION_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static final Timestamp CREATION_DATE_OLD = Timestamp.valueOf(LocalDateTime.now().minusDays(25));
    private static final long ACCESSES = 0;

    private static final long REPORT_ID = 1;
    private static final User CREATOR = new User.UserBuilder(EMAIL).pass(PASS).build();
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

    private User addCreatorToTable() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("pass", PASS);
        userValues.put("status", CREATOR_STATUS.getStatus());
        userValues.put("user_id", CREATOR_ID);
        jdbcUserInsert.execute(userValues);
        return entityManager.getReference(User.class, CREATOR_ID);
    }

    private News addTheNewsToTable() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("accesses", ACCESSES);
        newsValues.put("body", BODY);
        newsValues.put("creation_date", CREATION_DATE);
        newsValues.put("title", TITLE);
        newsValues.put("creator", CREATOR_ID);
        newsValues.put("subtitle", SUBTITLE);
        jdbcNewsInsert.execute(newsValues);
        return entityManager.getReference(News.class, NEWS_ID);
    }

    private News addOldNews() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID2);
        newsValues.put("accesses", ACCESSES);
        newsValues.put("body", BODY);
        newsValues.put("creation_date", CREATION_DATE_OLD);
        newsValues.put("title", TITLE);
        newsValues.put("creator", CREATOR_ID);
        newsValues.put("subtitle", SUBTITLE);
        jdbcNewsInsert.execute(newsValues);
        return entityManager.getReference(News.class, NEWS_ID);
    }

    private void addCategoryToOldNews() {
        Map<String, Object> categoryValues = new HashMap<>();
        categoryValues.put("category_id", CATEGORY.getId());
        categoryValues.put("news_id", NEWS_ID2);
        jdbcCategoryInsert.execute(categoryValues);
    }

    private void addCategoryToTheNews() {
        Map<String, Object> categoryValues = new HashMap<>();
        categoryValues.put("category_id", CATEGORY.getId());
        categoryValues.put("news_id", NEWS_ID);
        jdbcCategoryInsert.execute(categoryValues);
    }

    private void addReportToTheNews() {
        Map<String, Object> reportValues = new HashMap<>();
        reportValues.put("user_id", CREATOR_ID);
        reportValues.put("news_id", NEWS_ID);
        reportValues.put("reason", REPORT_REASON.name());
        reportValues.put("id", REPORT_ID);
        reportValues.put("report_date", CREATION_DATE);
        jdbcReportInsert.execute(reportValues);
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
        User user = addCreatorToTable();
        News optionalNews = newsJpaDao.create(new News.NewsBuilder(user,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(NEWS_ID));

        entityManager.flush();
        assertEquals(TITLE, optionalNews.getTitle());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetNewsById() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional<News> optionalNews = newsJpaDao.getById(NEWS_ID);

        assertEquals(NEWS_ID, optionalNews.get().getNewsId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByCreatorId() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional<News> optionalFullNews = newsJpaDao.getById(NEWS_ID);

        assertEquals(optionalFullNews.get().getCreatorId(), NEWS_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testDeleteNews() {
        addCreatorToTable();
        News news = addTheNewsToTable();

        newsJpaDao.deleteNews(news);

        entityManager.flush();

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetNewsByCategory(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();

        List<News> newsList = newsJpaDao.getNewsByCategoryNew(PAGE_SIZE, CATEGORY);

        assertEquals(1, newsList.size());
        assertTrue(newsList.get(0).getCategories().contains(CATEGORY));
    }

    @Test
    public void testGetNewsByCategoryTop(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();
        addOldNews();
        addCategoryToOldNews();

        List<News> newsList = newsJpaDao.getNewsByCategoryTop(PAGE_SIZE, CATEGORY, TimeConstraint.DAY);

        assertEquals(1, newsList.size());
        assertTrue(newsList.get(0).getCategories().contains(CATEGORY));
    }

    @Test
    public void testReportNews(){
        User creator = addCreatorToTable();
        News news = addTheNewsToTable();

        newsJpaDao.reportNews(news, creator, REPORT_REASON);
        entityManager.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REPORT_TABLE));
    }

    @Test
    public void testGetReportNewsDetail(){
        addCreatorToTable();
        addTheNewsToTable();
        addReportToTheNews();

        Page<ReportDetail> reportPage = newsJpaDao.getReportedNewsDetail(PAGE_SIZE, NEWS_ID);

        assertEquals(REPORT_REASON, reportPage.getContent().get(0).getReason());
    }

}
