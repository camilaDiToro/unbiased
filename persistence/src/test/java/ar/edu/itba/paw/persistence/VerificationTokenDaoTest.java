package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.user.VerificationToken;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

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
    private static final String TOKEN_TABLE = "email_verification_token";
    private static final int TOKEN_DURATION = 1;
    private static final String EMAIL = "user@gmail.com";
    private static final String TOKEN = "token";

    private User user;
    private static final LocalDateTime TOKEN_DUE_DATE = LocalDateTime.now().plusDays(TOKEN_DURATION);

    @Before
    public void setUp() {
        userDao = new UserJdbcDao(ds);
        verificationTokenDao = new VerificationTokenJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        user = createUser();
    }

    private User createUser() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        return userDao.create(usBuilder);
    }

    @Test
    public void testCreateVerificationToken() {
        VerificationToken token = verificationTokenDao.createEmailToken(user.getId(), TOKEN, TOKEN_DUE_DATE);

        assertEquals(user.getId(), token.getUserId());
        assertEquals(TOKEN, token.getToken());
        assertEquals(TOKEN_DUE_DATE, token.getExpiryDate());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }
    @Test
    public void testGetToken(){

        VerificationToken tokenVef = verificationTokenDao.createEmailToken(user.getId(), TOKEN, TOKEN_DUE_DATE);
        Optional<VerificationToken> token = verificationTokenDao.getEmailToken(TOKEN);
        token.ifPresent(opt -> assertEquals(token.get().getUserId(), tokenVef.getId()));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }

    @Test
    public void testFindByTokenFailed() {
        Optional<VerificationToken> token= verificationTokenDao.getEmailToken("othertoken");
        assertFalse(token.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }

}
