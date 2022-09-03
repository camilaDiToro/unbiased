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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {

    private static final String USER_TABLE = "users";
    private static final String EMAIL = "juan@gmail.com";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private UserJdbcDao userDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("userId");
    }

    @Test
    public void testCreateUser(){
//        // 1. precondiciones
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);
//
//        // 2. ejercitacion
//        User user = userDao.create(new User.UserBuilder(EMAIL).build());
//
//        // 3. validaciones
//        assertNotNull(user);
//        assertEquals(EMAIL, user.getEmail());
//        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,USER_TABLE));
    }

    @Test
    public void testGetUserByIdDoesntExist(){

        // 1. clear database
        // estaria mal userDao.deleteAll();
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        //2.
        Optional<User> mayBeUser = userDao.getUserById(1);

        //3.
        assertFalse(mayBeUser.isPresent());
    }

    @Test
    public void testGetUserByIdUserExists(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", EMAIL);
        Number key = jdbcInsert.executeAndReturnKey(userData);

        Optional<User> mayBeUser = userDao.getUserById(key.longValue());

        assertTrue(mayBeUser.isPresent());
        assertEquals(EMAIL, mayBeUser.get().getEmail());
    }
}
