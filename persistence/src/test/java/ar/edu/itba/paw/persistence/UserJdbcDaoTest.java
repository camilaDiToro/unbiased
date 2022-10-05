package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJdbcDaoTest {
    //TABLAS
    private static final String USER_TABLE = "users";
    protected static final String TOKEN_TABLE = "email_verification_token";
    protected static final String FOLLOWS_TABLE = "follows";

    //USER DATA
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@gmail.com";

    private JdbcTemplate jdbcTemplate;
    private UserJdbcDao userDao;
    private VerificationTokenDao verificationTokenDao;

    User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);

    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcDaoTest.class);

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        verificationTokenDao = new VerificationTokenJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateUser() {
        User user = userDao.create(usBuilder);
        Optional<User> optionalUser = userDao.getUserById(user.getId());

        optionalUser.ifPresent(opt -> assertEquals(EMAIL, optionalUser.get().getEmail()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + optionalUser.get().getId()));
    }

    @Test
    public void testFindById(){
        User us = userDao.create(usBuilder);
        Optional<User> optionalUser = userDao.getUserById(us.getId());

        optionalUser.ifPresent(opt -> assertEquals(optionalUser.get().getId(), us.getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + optionalUser.get().getId()));
    }

    @Test
    public void testFailFindByUserId() {
        Optional<User> optionalUser = userDao.getUserById(1L);

        assertFalse(optionalUser.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFindByEmail() {
        User us = userDao.create(usBuilder);
        Optional<User> optionalUser = userDao.getUserById(us.getId());

        optionalUser.ifPresent(opt -> assertEquals(EMAIL, optionalUser.get().getEmail()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + optionalUser.get().getId()));

    }

    @Test
    public void testFailFindByEmail() {
        Optional<User> usr = userDao.findByEmail(EMAIL);

        assertFalse(usr.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testVerifyEmail(){
        User us = userDao.create(usBuilder);
        VerificationToken vt = verificationTokenDao.createEmailToken(us.getId(), "token", LocalDateTime.now().plusDays(1));
        userDao.verifyEmail(vt.getUserId());
        Optional<User> optionalUser = userDao.getUserById(us.getId());

        optionalUser.ifPresent(opt-> assertEquals(UserStatus.REGISTERED, optionalUser.get().getStatus()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + optionalUser.get().getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }

    @Test
    public void testFindByUsername() {
        User us = userDao.create(usBuilder);
        userDao.updateUsername(us, USERNAME);
        Optional<User> optionalUser = userDao.getUserById(us.getId());

        optionalUser.ifPresent(opt-> assertEquals(USERNAME, optionalUser.get().getUsername()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + optionalUser.get().getId()));
    }

    @Test
    public void testFailFindByUsername() {
        final Optional<User> user = userDao.findByUsername(USERNAME);
        assertFalse(user.isPresent());
    }

    @Test
    public void testIsFollowing(){
        User.UserBuilder followBuilder = new User.UserBuilder("follow@mail.com");
        User us = userDao.create(usBuilder);
        User follow = userDao.create(followBuilder);
        userDao.addFollow(us.getId(), follow.getId());
        Optional<User> optionalUser = userDao.getUserById(us.getId());
        boolean userFollow = userDao.isFollowing(optionalUser.get().getId(), follow.getId());

        optionalUser.ifPresent(opt-> assertTrue(userFollow));
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + us.getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "user_id = " + follow.getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FOLLOWS_TABLE));
    }
}