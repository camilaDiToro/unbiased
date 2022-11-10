package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import org.junit.Assert;
import org.junit.Assert.*;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJpaDaoTest {
    //USER DATA
    private static final String USERNAME = "username";
    private static final String UPDATE_USERNAME = "updated_username";
    private static final String EMAIL = "user@gmail.com";
    private static final UserStatus USER_STATUS = UserStatus.UNABLE;
    private static final long USER_ID = 2;
    private static final User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);

    //Follower DATA
    private static final String F_USERNAME = "username2";
    private static final String F_EMAIL = "user2@gmail.com";
    private static final UserStatus F_STATUS = UserStatus.REGISTERED;
    private static final long F_ID = 7;

    private static final String USERS_TABLE = "users";

    @Autowired
    private DataSource ds;
    private UserJpaDao userDao;
    private SimpleJdbcInsert jdbcUserInsert;
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    private void createUser() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("username", USERNAME);
        userValues.put("status", USER_STATUS.getStatus());
        userValues.put("user_id", USER_ID);
        jdbcUserInsert.execute(userValues);
    }

    @Before
    public void setUp() {
        userDao = new UserJpaDao();
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE);
    }

    @Test
    public void testCreateUser() {
        User user = userDao.create(usBuilder);

        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testFindById() {
        createUser();
        Optional<User> user = userDao.getUserById(USER_ID);

        Assert.assertTrue(user.isPresent());
        //Assert.assertEquals(USER_ID, user.getId());
        //Assert.assertEquals(EMAIL, user.getEmail());
    }
}
