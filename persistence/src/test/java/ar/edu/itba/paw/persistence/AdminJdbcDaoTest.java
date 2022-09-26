package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AdminJdbcDaoTest {
    private AdminJdbcDao adminDao;
    private NewsJdbcDao newsDao;
    private UserJdbcDao userDao;
    private RoleJdbcDao roleDao;
    private CategoryDao categoryDao;
    @Autowired
    private DataSource ds;
    protected JdbcTemplate jdbcTemplate;
    //TABLES
    protected static final String ROLE_TABLE = "user_role";
    protected static final String REPORT_TABLE = "report";

    protected static final String EMAIL = "user@gmail.com";
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";
    protected static final int PAGE_SIZE = 1;

    private User getMockUser() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        return userDao.create(usBuilder);
    }

    private News getMockNews() {
        User user = getMockUser();
        News.NewsBuilder nsBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        return newsDao.create(nsBuilder);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        newsDao = new NewsJdbcDao(ds, categoryDao);
        userDao = new UserJdbcDao(ds);
        categoryDao = new CategoryJdbcDao(ds);
        roleDao = new RoleJdbcDao(ds);
        adminDao = new AdminJdbcDao(ds);
    }

    @Test
    public void testMakeUserAdmin(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLE_TABLE);

        User user = getMockUser();
        adminDao.makeUserAdmin(user);
        List<String> roleList = roleDao.getRoles(user);

        assertEquals(Role.ADMIN.getRole(), roleList.get(0));
    }

    @Test
    public void testReportedNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, REPORT_TABLE);

        News news = getMockNews();
        adminDao.reportNews(news, 1L, ReportReason.LIE);

        assertTrue(adminDao.hasReported(news, 1L));
    }

    @Test
    public void testGetReportedNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, REPORT_TABLE);

        News news = getMockNews();
        adminDao.reportNews(news, 1L, ReportReason.LIE);
        Page<ReportedNews> reportList = adminDao.getReportedNews(PAGE_SIZE, NewsOrder.NEW);

        assertEquals(PAGE_SIZE, reportList.getTotalPages());
    }

    @Test
    public void testGetReportedNewsDetail(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, REPORT_TABLE);

        News news = getMockNews();
        adminDao.reportNews(news, 1L, ReportReason.LIE);
        Page<ReportDetail> reportList = adminDao.getReportedNewsDetail(PAGE_SIZE, news);

        assertEquals(ReportReason.LIE, reportList.getContent().get(0).getReason());
    }
}
