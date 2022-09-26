package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.PositivityStats;
import ar.edu.itba.paw.model.user.User;
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

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User
            .UserBuilder(rs.getString("email"))
            .username(rs.getString("username"))
            .userId(rs.getLong("user_id"))
            .pass(rs.getString("pass"))
            .imageId(rs.getLong("image_id") == 0 ? null : rs.getLong("image_id"))
            .status(rs.getString("status"))
            .positivity(new PositivityStats(rs.getInt("upvotes"),rs.getInt("downvotes"))).build();

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("user_id");
    }

    @Override
    public Optional<User> getUserById(long id) {
        List<User> query= jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN user_positivity WHERE user_id = ?",
                new Object[] { id }, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public User create(User.UserBuilder userBuilder) {

        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", userBuilder.getEmail());
        userData.put("status", userBuilder.getStatus().getStatus());
        userData.put("pass", userBuilder.getPass());
        userData.put("image_id",userBuilder.getImageId());
        userData.put("username",userBuilder.getUsername());

        final long userId = jdbcInsert.executeAndReturnKey(userData).longValue();
        return userBuilder.userId(userId).build();
    }

    @Override
    public User createIfNotExists(User.UserBuilder userBuilder) {
        Optional<User> user = findByEmail(userBuilder.getEmail());
        if(user.isPresent()){
            return user.get();
        }
        return create(userBuilder);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN user_positivity WHERE email = ?",
                new Object[] { email }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN user_positivity WHERE username = ?",
                new Object[] { username }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void verifyEmail(long id) {
        jdbcTemplate.update("UPDATE users SET status = 'REGISTERED' WHERE user_id = ?", id);
    }

    @Override
    public void updateUsername(User user, String username) {
        jdbcTemplate.update("UPDATE users SET username = ? WHERE user_id = ?", username, user.getId());
    }

    @Override
    public void updateImage(User user, Long imageId) {
        jdbcTemplate.update("UPDATE users SET image_id = ? WHERE user_id = ?", imageId, user.getId());
    }

    public List<User> getAll(int page){
        return jdbcTemplate.query("SELECT * FROM Users NATURAL JOIN user_positivity LIMIT 10 OFFSET ?", new Object[]{(page-1)*10},ROW_MAPPER);
    }

    @Override
    public List<User> getTopCreators(int qty) {
        return jdbcTemplate.query("WITH interactions AS (SELECT creator AS user_id, count(*) AS interaction_count FROM upvotes JOIN news ON upvotes.news_id = news.news_id " +
                "WHERE DATE(interaction_date) = CURRENT_DATE GROUP BY creator LIMIT ?) SELECT * FROM interactions NATURAL JOIN users NATURAL JOIN user_positivity ORDER BY interaction_count DESC", new Object[]{qty},ROW_MAPPER);

    }
}
