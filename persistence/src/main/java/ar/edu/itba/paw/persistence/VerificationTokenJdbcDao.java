package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class VerificationTokenJdbcDao implements VerificationTokenDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final static RowMapper<VerificationToken> TOKEN_ROW_MAPPER = (rs, rowNum) -> new VerificationToken(rs.getLong("token_id"),
            rs.getString("token"),
            rs.getLong("user_id"),
            rs.getTimestamp("expiration_date").toLocalDateTime());

    @Autowired
    public VerificationTokenJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("email_verification_token").usingGeneratedKeyColumns("token_id");
    }


    @Override
    public VerificationToken createEmailToken(long userId, String token, LocalDateTime expiryDate) {
        final Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("user_id", userId);
        tokenData.put("token", token);
        tokenData.put("expiration_date", Timestamp.valueOf(expiryDate));

        final long tokenId = simpleJdbcInsert.executeAndReturnKey(tokenData).longValue();
        return  new VerificationToken(tokenId, token, userId, expiryDate);
    }

    @Override
    public Optional<VerificationToken> getEmailToken(String token) {
        return jdbcTemplate.query("SELECT * FROM email_verification_token WHERE token = ?", new Object[]{token},
                TOKEN_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void deleteEmailToken(long userId) {
        jdbcTemplate.update("DELETE FROM email_verification_token WHERE user_id = ? ", userId);
    }

}
