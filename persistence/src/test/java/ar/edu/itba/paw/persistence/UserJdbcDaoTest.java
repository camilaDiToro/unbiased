package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
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
    private static final String USER_TABLE = "users";
    private static final String EMAIL = "juan@gmail.com";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private UserJdbcDao userDao;

    User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
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
    public void testGetUserByIdDoesntExist() {

        // 1. clear database
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        //2.
        Optional<User> mayBeUser = userDao.getUserById(1);

        //3.
        assertFalse(mayBeUser.isPresent());
    }

    @Test
    public void testFailFindById() {
        final Optional<User> usr = userDao.getUserById(80);
        assertFalse(usr.isPresent());
    }

    @Test
    public void testGetUserByIdUserExists() {
        //precondicion
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        //ejercitacion
//        final Map<String, Object> userData = new HashMap<>();
//        userData.put("username", EMAIL);
//        Number key = jdbcInsert.executeAndReturnKey(userData);
        User us = userDao.create(usBuilder);

        Optional<User> mayBeUser = userDao.getUserById(us.getId());

        assertTrue(mayBeUser.isPresent());
        assertEquals(EMAIL, mayBeUser.get().getEmail());
    }

    @Test
    public void testFailFindByEmail() {
        final Optional<User> usr = userDao.findByEmail(EMAIL);
        assertFalse(usr.isPresent());
    }

}