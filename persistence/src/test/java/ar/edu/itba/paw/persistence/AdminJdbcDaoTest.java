package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.Role;
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
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/*
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AdminJdbcDaoTest {

    @Autowired
    private DataSource ds;
    private AdminJdbcDao adminDao;
    private RoleJdbcDao roleDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcNewsInsert;

    //TABLES
    private static final String USER_TABLE = "users";
    private static final String ROLE_TABLE = "user_role";
    private static final String REPORT_TABLE = "report";
    private static final String NEWS_TABLE = "news";

    //USER DATA
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@gmail.com";
    private static final UserStatus USER_STATUS = UserStatus.UNABLE;
    private static final long USER_ID = 6;
    private static final User USER = new User.UserBuilder(EMAIL).userId(USER_ID).username(USERNAME).status(USER_STATUS.getStatus()).build();

    // NEWS DATA
    private static final String TITLE = "titulo";
    private static final String SUBTITLE = "subtitulo";
    private static final String BODY = "cuerpo";
    private static final long NEWS_ID = 12;
    private static final Timestamp NEWS_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static final long NEWS_ACCESSES = 0;
    private static final News NEWS = new News.NewsBuilder(USER,BODY,TITLE,SUBTITLE).newsId(NEWS_ID).build();


    private void createUser() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("username", USERNAME);
        userValues.put("status", USER_STATUS.getStatus());
        userValues.put("user_id", USER_ID);
        jdbcUserInsert.execute(userValues);
    }

    private void createNews() {
        createUser();
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("body", BODY);
        newsValues.put("title", TITLE);
        newsValues.put("subtitle", SUBTITLE);
        newsValues.put("creator", USER_ID);
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("creation_date", NEWS_DATE);
        newsValues.put("accesses", NEWS_ACCESSES);
        jdbcNewsInsert.execute(newsValues);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcNewsInsert = new SimpleJdbcInsert(ds).withTableName(NEWS_TABLE);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
        roleDao = new RoleJdbcDao(ds);
        adminDao = new AdminJdbcDao(ds);
    }

    @Test
    public void testMakeUserAdmin(){
        createUser();
        adminDao.makeUserAdmin(USER);
        List<String> roleList = roleDao.getRoles(USER.getId());

        assertEquals(Role.ADMIN.getRole(), roleList.get(0));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, ROLE_TABLE));
    }

    @Test
    public void testReportedNews(){
        createNews();
        adminDao.reportNews(NEWS, NEWS.getCreatorId(), ReportReason.LIE);

        assertTrue(adminDao.hasReported(NEWS, NEWS.getCreatorId()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REPORT_TABLE, "news_id = " + NEWS.getNewsId()));
    }

    @Test
    public void testGetReportedNews(){
        createNews();
        adminDao.reportNews(NEWS, NEWS.getCreatorId(), ReportReason.LIE);
        adminDao.getReportedNews(1, ReportOrder.REP_DATE_DESC);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REPORT_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REPORT_TABLE, "news_id = " + NEWS.getNewsId()));

    }

    @Test
    public void testGetReportedNewsDetail(){
        createNews();
        adminDao.reportNews(NEWS, NEWS.getCreatorId(), ReportReason.LIE);
        Page<ReportDetail> reportList = adminDao.getReportedNewsDetail(1, NEWS);

        assertEquals(ReportReason.LIE, reportList.getContent().get(0).getReason());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REPORT_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REPORT_TABLE, "news_id = " + NEWS.getNewsId()));
    }
}
 */
