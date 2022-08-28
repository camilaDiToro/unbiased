package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(rs.getLong("user_id"), rs.getString("email"), rs.getLong("data_id"));

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("user_id");
    }

    @Override
    public Optional<User> getUserById(long id) {
        List<User> query= jdbcTemplate.query("SELECT * FROM Users WHERE user_id = ?",
                new Object[] { id }, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public User create(String email) {

        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        final long userId = jdbcInsert.executeAndReturnKey(userData).longValue();
        return new User(userId, email, null);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM Users WHERE email = ?",
                new Object[] { email }, ROW_MAPPER).stream().findFirst();
    }

    public List<User> getAll(int page){
        return jdbcTemplate.query("SELECT * FROM Users LIMIT 10 OFFSET ?", new Object[]{(page-1)*10},ROW_MAPPER);
    }
}
