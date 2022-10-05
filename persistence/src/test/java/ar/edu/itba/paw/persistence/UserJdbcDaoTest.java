package ar.edu.itba.paw.persistence;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJdbcDaoTest {

    //TABLES
    private static final String USER_TABLE = "users";
    protected static final String FOLLOWS_TABLE = "follows";

    //USER DATA
    private static final String USERNAME = "username";
    private static final String UPDATE_USERNAME = "updated_username";
    private static final String EMAIL = "user@gmail.com";
    private static final UserStatus USER_STATUS = UserStatus.UNABLE;
    private static final long USER_ID = 6;
    private static final User.UserBuilder usBuilder = new User.UserBuilder(EMAIL).userId(USER_ID).username(USERNAME).status(USER_STATUS.getStatus());

    //Follower DATA
    private static final String F_USERNAME = "username2";
    private static final String F_EMAIL = "user2@gmail.com";
    private static final UserStatus F_STATUS = UserStatus.REGISTERED;
    private static final long F_ID = 7;

    private JdbcTemplate jdbcTemplate;
    private UserJdbcDao userDao;

    @Autowired
    private DataSource ds;
    private SimpleJdbcInsert jdbcUserInsert;

    private void createUser() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("username", USERNAME);
        userValues.put("status", USER_STATUS.getStatus());
        userValues.put("user_id", USER_ID);
        jdbcUserInsert.execute(userValues);
    }

    private void createFollower() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", F_EMAIL);
        userValues.put("username", F_USERNAME);
        userValues.put("status", F_STATUS.getStatus());
        userValues.put("user_id", F_ID);
        jdbcUserInsert.execute(userValues);
    }

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
    }

    @Test
    public void testCreateUser() {
        User user = userDao.create(usBuilder);

        assertEquals(EMAIL, user.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + user.getId()));
    }

    @Test
    public void testFindById(){
        createUser();
        User user = userDao.getUserById(USER_ID).get();

        assertEquals(USER_ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testFailFindByUserId() {
        Optional<User> optionalUser = userDao.getUserById(1L);

        assertFalse(optionalUser.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFindByEmail() {
        createUser();
        User user = userDao.findByEmail(EMAIL).get();

        assertEquals(USER_ID, user.getId());
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testFailFindByEmail() {
        createUser();
        Optional<User> usr = userDao.findByEmail(EMAIL+"otheremail");

        assertFalse(usr.isPresent());
    }

    @Test
    public void testVerifyEmail(){
        createUser();
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + USER_ID +" AND status = \'" + UserStatus.UNABLE.getStatus() + "\'"));

        userDao.verifyEmail(USER_ID);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + USER_ID +" AND status = \'" + UserStatus.REGISTERED.getStatus() + "\'"));
    }

    @Test
    public void testUpdateUsername() {
        createUser();
        userDao.updateUsername(usBuilder.build(), UPDATE_USERNAME);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + USER_ID +" AND username = \'" + USERNAME + "\'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + USER_ID +" AND username = \'" + UPDATE_USERNAME + "\'"));
    }

    @Test
    public void testFailFindByUsername() {
        createUser();
        Optional<User> user = userDao.findByUsername(UPDATE_USERNAME);
        assertFalse(user.isPresent());
    }

    @Test
    public void testAdd(){
        createUser();
        createFollower();
        userDao.addFollow(F_ID, USER_ID);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, FOLLOWS_TABLE, "user_id = " + F_ID + " AND follows = " + USER_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FOLLOWS_TABLE));
    }

    @Test
    public void testIsFollowing(){
        createUser();
        createFollower();
        userDao.addFollow(F_ID, USER_ID);
        boolean userFollow = userDao.isFollowing(F_ID, USER_ID);

        assertTrue(userFollow);
    }
}