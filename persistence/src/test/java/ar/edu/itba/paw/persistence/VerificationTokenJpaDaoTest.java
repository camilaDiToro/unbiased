package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import static org.junit.Assert.*;

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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@Rollback
public class VerificationTokenJpaDaoTest {
    @Autowired
    private DataSource ds;
    @Autowired
    private VerificationTokenJpaDao verificationTokenDao;

    @PersistenceContext
    EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;
    private static final String USERS_TABLE = "users";
    private static final String TOKEN_TABLE = "email_verification_token";
    private static final int TOKEN_ID = 1;
    private static final Timestamp EXPIRATION_DATE = Timestamp.valueOf(LocalDateTime.now());
    //USER DATA
    private static final String USERNAME = "username";
    private static final String PASS = "pass";
    private static final String EMAIL = "user@gmail.com";
    private static final UserStatus USER_STATUS = UserStatus.UNABLE;
    private static final long USER_ID = 1;
    private static final String TOKEN = "token";
    private static final String OTHER_TOKEN = "othertoken";

    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcVerificationTokenInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE);
        jdbcVerificationTokenInsert = new SimpleJdbcInsert(ds).withTableName(TOKEN_TABLE);
    }

    private User addUsertoTable() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("pass", PASS);
        userValues.put("username", USERNAME);
        userValues.put("status", USER_STATUS.getStatus());
        userValues.put("user_id", USER_ID);
        jdbcUserInsert.execute(userValues);
        return entityManager.getReference(User.class, USER_ID);
    }

    private void addTokentoTable() {
        Map<String, Object> tokenValues = new HashMap<>();
        tokenValues.put("token_id", TOKEN_ID);
        tokenValues.put("expiration_date", EXPIRATION_DATE);
        tokenValues.put("token", TOKEN);
        tokenValues.put("user_id", USER_ID);
        jdbcVerificationTokenInsert.execute(tokenValues);
    }

    @Test
    public void testCreateVerificationToken() {
        User user = addUsertoTable();

        VerificationToken token = verificationTokenDao.createEmailToken(user.getId(), TOKEN, EXPIRATION_DATE.toLocalDateTime());

        assertEquals(user.getId(), token.getUserId());
        assertEquals(TOKEN, token.getToken());
        assertEquals(EXPIRATION_DATE.toLocalDateTime(), token.getExpiryDate());
    }

    @Test
    public void testGetToken(){
        addTokentoTable();

        Optional<VerificationToken> token = verificationTokenDao.getEmailToken(TOKEN);

        assertEquals(token.get().getUserId(), USER_ID);
        assertEquals(token.get().getToken(), TOKEN);
        assertEquals(token.get().getExpiryDate(), EXPIRATION_DATE.toLocalDateTime());
    }

    @Test
    public void testFindByTokenFailed() {
        addTokentoTable();

        Optional<VerificationToken> token= verificationTokenDao.getEmailToken(OTHER_TOKEN);

        assertFalse(token.isPresent());
    }

}
