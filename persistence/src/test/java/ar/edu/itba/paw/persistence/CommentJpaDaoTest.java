package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback
public class CommentJpaDaoTest {
    private static final String date = "2022-11-12 20:45:00";
    //USER DATA
    private static final String EMAIL = "user@gmail.com";
    private static final String EMAIL2 = "user@gmail.com";
    private static final String PASS = "pass";
    private static final long CREATOR_ID = 1;
    private static final UserStatus CREATOR_STATUS = UserStatus.REGISTERED;
    //NEWS DATA
    private static final long NEWS_ID = 1;
    private static final String TITLE = "titulo";
    private static final String SUBTITLE = "subtitulo";
    private static final String BODY = "cuerpo";
    private static final int PAGE_SIZE = 1;
    private static final Timestamp CREATION_DATE = Timestamp.valueOf(date);
    private static final long ACCESSES = 0;
    private static final User CREATOR = new User.UserBuilder(EMAIL).pass(PASS).build();
    private static final User CREATOR2 = new User.UserBuilder(EMAIL2).pass(PASS).build();
    private static final News NEWS = new News.NewsBuilder(CREATOR,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(NEWS_ID).build();
    private static final News NEWS2 = new News.NewsBuilder(CREATOR2,BODY,TITLE,SUBTITLE).creationDate(CREATION_DATE.toLocalDateTime()).newsId(2).build();
    //COMMENT DATA
    private static final long COMMENT_ID = 1;
    private static final String COMMENT = "Comment";
    private static final Timestamp COMMENT_DATE = Timestamp.valueOf(date);
    private static final boolean DELETED= false;
    //REPORT DATA
    private static final ReportReason REPORT_REASON = ReportReason.LIE;
    //TABLES
    private static final String NEWS_TABLE = "news";
    private static final String USER_TABLE = "users";
    private static final String COMMENT_TABLE = "comments";
    private static final String REPORT_TABLE = "comment_report";

    @Autowired
    private CommentJpaDao commentDao;
    @Autowired
    private DataSource ds;

    @PersistenceContext
    EntityManager entityManager;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcNewsInsert;
    private SimpleJdbcInsert jdbcCommentInsert;
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

    private void addCommentFromCreatorToTheNews() {
        Map<String, Object> commentValues = new HashMap<>();
        commentValues.put("id", COMMENT_ID);
        commentValues.put("news_id", NEWS_ID);
        commentValues.put("user_id", CREATOR_ID);
        commentValues.put("comment", COMMENT);
        commentValues.put("commented_date", COMMENT_DATE);
        commentValues.put("deleted", DELETED);
        jdbcCommentInsert.execute(commentValues);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
        jdbcNewsInsert = new SimpleJdbcInsert(ds).withTableName(NEWS_TABLE);
        jdbcCommentInsert = new SimpleJdbcInsert(ds).withTableName(COMMENT_TABLE);
        jdbcReportInsert = new SimpleJdbcInsert(ds).withTableName(REPORT_TABLE);
    }

    @Test
    public void testAddComment() {
        addCreatorToTable();
        addTheNewsToTable();
        addCommentFromCreatorToTheNews();

        commentDao.addComment(CREATOR, NEWS, COMMENT);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
    }

    @Test
    public void testFindCommentById(){
        addCreatorToTable();
        addTheNewsToTable();
        addCommentFromCreatorToTheNews();

        Comment comment = commentDao.getCommentById(COMMENT_ID).get();

        assertEquals(comment.getComment(), COMMENT);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COMMENT_TABLE,  "news_id = " + NEWS_ID));
    }

    @Test
    public void testFailFindCommentById(){
        Optional<Comment> comment = commentDao.getCommentById(2L);

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
    }

    @Test
    public void testGetNewComments(){
        addCreatorToTable();
        addTheNewsToTable();
        addCommentFromCreatorToTheNews();

        Page<Comment> commentPage = commentDao.getNewComments(NEWS_ID, PAGE_SIZE);

        assertEquals(commentPage.getContent().get(0).getComment(), COMMENT);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE,  "news_id = " + NEWS_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COMMENT_TABLE,  "news_id = " + NEWS_ID));
    }

    @Test
    public void testReportComment(){
        commentDao.reportComment(new Comment(CREATOR,COMMENT,NEWS), CREATOR, REPORT_REASON);
        entityManager.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, REPORT_TABLE));
    }

    @Test
    public void testGetDeletedComments() {
        addCreatorToTable();
        addTheNewsToTable();
        addCommentFromCreatorToTheNews();

        Comment comment = commentDao.getCommentById(COMMENT_ID).get();
        commentDao.deleteComment(comment.getId());

        assertEquals(comment.getDeleted(), true);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
    }
}
