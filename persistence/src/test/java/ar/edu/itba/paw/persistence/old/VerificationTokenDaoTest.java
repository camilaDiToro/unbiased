package ar.edu.itba.paw.persistence.old;


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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
/*
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class VerificationTokenDaoTest {

    @Autowired
    private DataSource ds;
    private JdbcTemplate jdbcTemplate;
    private VerificationTokenDao verificationTokenDao;
    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcTokenInsert;

    //TABLES
    private static final String USER_TABLE = "users";
    private static final String TOKEN_TABLE = "email_verification_token";

    // TOKEN DATA
    private static final int TOKEN_DURATION = 1;
    private static final long TOKEN_ID = 3;
    private static final String TOKEN = "token";
    private static final LocalDateTime TOKEN_DUE_DATE = LocalDateTime.now().plusDays(TOKEN_DURATION);

    //USER DATA
    private static final String USERNAME = "username";
    private static final UserStatus USER_STATUS = UserStatus.UNABLE;
    private static final String EMAIL = "user@gmail.com";
    private static final long USER_ID = 6;


    @Before
    public void setUp() {
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
        verificationTokenDao = new VerificationTokenJdbcDao(ds);
        jdbcTokenInsert =  new SimpleJdbcInsert(ds).withTableName(TOKEN_TABLE);
        jdbcTemplate = new JdbcTemplate(ds);
        createUser();
    }

    private void createUser() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("username", USERNAME);
        userValues.put("status", USER_STATUS.getStatus());
        userValues.put("user_id", USER_ID);
        jdbcUserInsert.execute(userValues);
    }

    private void createToken() {
        final Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("user_id", USER_ID);
        tokenData.put("token_id", TOKEN_ID);
        tokenData.put("token", TOKEN);
        tokenData.put("expiration_date", Timestamp.valueOf(TOKEN_DUE_DATE));
        jdbcTokenInsert.execute(tokenData);
    }

    @Test
    public void testCreateVerificationToken() {
        VerificationToken token = verificationTokenDao.createEmailToken(USER_ID, TOKEN, TOKEN_DUE_DATE);

        assertEquals(USER_ID, token.getUserId());
        assertEquals(TOKEN, token.getToken());
        assertEquals(TOKEN_DUE_DATE, token.getExpiryDate());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }

    @Test
    public void testGetToken(){
        createToken();
        Optional<VerificationToken> token = verificationTokenDao.getEmailToken(TOKEN);

        assertEquals(token.get().getUserId(), USER_ID);
        assertEquals(token.get().getToken(), TOKEN);
        assertEquals(token.get().getExpiryDate(), TOKEN_DUE_DATE);
    }

    @Test
    public void testFindByTokenFailed() {
        Optional<VerificationToken> token= verificationTokenDao.getEmailToken("othertoken");
        assertFalse(token.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, TOKEN_TABLE));
    }

}
*/