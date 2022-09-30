package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
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
import java.io.IOException;
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

    @Autowired
    private DataSource ds;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        verificationTokenDao = new VerificationTokenJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("userId");
    }

    @Test
    public void testCreateUser() {
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        // 2. ejercitacion
        User user = userDao.create(usBuilder);

        // 3. validaciones
        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFindById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        User us = userDao.create(usBuilder);
        Optional<User> optionalUser = userDao.getUserById(us.getId());

        optionalUser.ifPresent(opt -> assertEquals(optionalUser.get().getId(), us.getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFailFindByUserId() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> mayBeUser = userDao.getUserById(1L);
        assertFalse(mayBeUser.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFindByEmail() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        //ejercitacion
//        final Map<String, Object> userData = new HashMap<>();
//        userData.put("username", EMAIL);
//        Number key = jdbcInsert.executeAndReturnKey(userData);
        User us = userDao.create(usBuilder);

        Optional<User> optionalUser = userDao.getUserById(us.getId());

        optionalUser.ifPresent(opt -> assertEquals(EMAIL, optionalUser.get().getEmail()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFailFindByEmail() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> usr = userDao.findByEmail(EMAIL);

        assertFalse(usr.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testVerifyEmail(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TOKEN_TABLE);

        User us = userDao.create(usBuilder);
        VerificationToken vt = verificationTokenDao.createEmailToken(us.getId(), "token", LocalDateTime.now().plusDays(1));
        assertNotNull(vt);
        userDao.verifyEmail(vt.getUserId());

        assertEquals(UserStatus.REGISTERED, us.getStatus());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }

    @Test
    public void testFindByUsername() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        User us = userDao.create(usBuilder);
        userDao.updateUsername(us, USERNAME);

        assertEquals(USERNAME, us.getUsername());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testFailFindByUsername() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        final Optional<User> user = userDao.findByUsername("username");

        assertFalse(user.isPresent());
    }

    @Test
    public void testTotalUsers(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        User us = userDao.create(usBuilder);
        List<User> users = userDao.getAll(1);

        assertEquals(1, users.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testIsFollowing(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, FOLLOWS_TABLE);

        User us = userDao.create(usBuilder);
        userDao.addFollow(us.getId(), 1);
        boolean userFollow = userDao.isFollowing(us.getId(), 1);

        assertTrue(userFollow);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FOLLOWS_TABLE));

    }
}