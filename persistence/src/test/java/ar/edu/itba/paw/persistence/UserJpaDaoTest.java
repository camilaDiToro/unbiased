package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import static org.junit.Assert.*;
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
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJpaDaoTest {
    //USER DATA
    private static final String USERNAME = "username";
    private static final String PASS = "pass";
    private static final String EMAIL = "user@gmail.com";
    private static final UserStatus USER_STATUS = UserStatus.UNABLE;
    private static final PositivityStats USER_POSITIVITY= new PositivityStats(1,1);
    private static final long USER_ID = 1;
    private static final User.UserBuilder usBuilder = new User.UserBuilder(EMAIL).pass(PASS).username(USERNAME).status(USER_STATUS.getStatus()).positivity(USER_POSITIVITY);

    //Follower DATA
    private static final String F_USERNAME = "username2";
    private static final String F_PASS = "fpass";
    private static final String F_EMAIL = "user2@gmail.com";
    private static final UserStatus F_STATUS = UserStatus.REGISTERED;
    private static final long F_ID = 2;

    private static final String USERS_TABLE = "users";
    protected static final String FOLLOWS_TABLE = "follows";


    @Autowired
    private DataSource ds;
    @Autowired
    private UserJpaDao userDao;
    private SimpleJdbcInsert jdbcUserInsert;
    private JdbcTemplate jdbcTemplate;

    private void addUsertoTable() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("pass", PASS);
        userValues.put("username", USERNAME);
        userValues.put("status", USER_STATUS.getStatus());
        userValues.put("user_id", USER_ID);
        jdbcUserInsert.execute(userValues);
    }

    private void addTheFollowToTable() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", F_EMAIL);
        userValues.put("pass", F_PASS);
        userValues.put("username", F_USERNAME);
        userValues.put("status", F_STATUS.getStatus());
        userValues.put("user_id", F_ID);
        jdbcUserInsert.execute(userValues);
    }


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE);
    }

    @Test
    public void testCreate() {
        User user = userDao.create(usBuilder);

        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testFindUserById() {
        addUsertoTable();
        User user = userDao.getUserById(USER_ID).get();

        assertEquals(USER_ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testFindByEmail() {
        addUsertoTable();
        User user = userDao.findByEmail(EMAIL).get();

        assertEquals(USER_ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testFindUserByUsername() {
        addUsertoTable();
        User user = userDao.findByUsername(USERNAME).get();

        assertEquals(USER_ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testVerifyEmail() {
        addUsertoTable();
        userDao.verifyEmail(USER_ID);
        User user = userDao.getUserById(USER_ID).get();

        assertEquals(USER_STATUS.REGISTERED.getStatus(), user.getStatus().getStatus());
        //assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USERS_TABLE, String.format("user_id = '%s' AND status = '%s'", USER_ID, USER_STATUS.REGISTERED.getStatus())));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testAddFollow(){
        addUsertoTable();
        addTheFollowToTable();
        userDao.addFollow(USER_ID, F_ID);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

}
