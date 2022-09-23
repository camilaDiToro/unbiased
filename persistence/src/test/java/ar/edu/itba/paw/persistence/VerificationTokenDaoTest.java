package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.user.User;
import org.junit.Assert;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertFalse;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class VerificationTokenDaoTest {
    @Autowired
    private DataSource ds;
    private VerificationTokenDao verificationTokenDao;
    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;
    protected static final String TOKEN_TABLE = "email_verification_token";
    private static final int TOKEN_DURATION = 1;

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        verificationTokenDao = new VerificationTokenJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
    }

    private User getMockUser() {
        String EMAIL = "juan@gmail.com";
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        User user = userDao.createIfNotExists(usBuilder);
        return user;
    }

    @Test
    public void testCreateVerificationToken() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TOKEN_TABLE);

        User user = getMockUser();
        Optional<User> optionalUser = userDao.getUserById(user.getId());
        LocalDateTime date = LocalDateTime.now().plusDays(TOKEN_DURATION);

        if (optionalUser.isPresent()) {
            VerificationToken token = verificationTokenDao.createEmailToken(optionalUser.get().getId(), "token", date);

            Assert.assertEquals(optionalUser.get().getId(), token.getUserId());
            Assert.assertEquals("token", token.getToken());
            Assert.assertEquals(date, token.getExpiryDate());
        }
    }
    @Test
    public void testGetToken(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TOKEN_TABLE);

        User user = getMockUser();
        Optional<User> optionalUser = userDao.getUserById(user.getId());

        if(optionalUser.isPresent()){
            Optional<VerificationToken> token= verificationTokenDao.getEmailToken("token");
            Assert.assertEquals(token.get().getUserId(), optionalUser.get().getId());
        }
    }

    @Test
    public void testFindByTokenFailed() {
        Optional<VerificationToken> token= verificationTokenDao.getEmailToken("token");
        assertFalse(token.isPresent());
    }

}
