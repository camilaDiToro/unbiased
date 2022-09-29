package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {
    private static final double USERS_PAGE_SIZE = 12.0;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert followJdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User
            .UserBuilder(rs.getString("email"))
            .username(rs.getString("username"))
            .userId(rs.getLong("user_id"))
            .pass(rs.getString("pass"))
            .imageId(rs.getLong("image_id") == 0 ? null : rs.getLong("image_id"))
            .status(rs.getString("status"))
            .positivity(new PositivityStats(rs.getInt("upvotes"),rs.getInt("downvotes"))).build();

    private final static RowMapper<Integer> ROW_COUNT_MAPPER = (rs, rowNum) -> rs.getInt("userCount");

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("user_id");
        followJdbcInsert = new SimpleJdbcInsert(ds).withTableName("follows");
    }

    @Override
    public Optional<User> getUserById(long id) {
        List<User> query= jdbcTemplate.query("SELECT * FROM Users NATURAL LEFT JOIN user_positivity WHERE user_id = ?",
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
        return jdbcTemplate.query("SELECT * FROM Users NATURAL LEFT JOIN user_positivity WHERE email = ?",
                new Object[] { email }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM Users NATURAL LEFT JOIN user_positivity WHERE username = ?",
                new Object[] { username }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void verifyEmail(long id) {
        jdbcTemplate.update("UPDATE users SET status = 'REGISTERED' WHERE user_id = ?", id);
    }

    @Override
    public void updateUsername(long userId, String username) {
        jdbcTemplate.update("UPDATE users SET username = ? WHERE user_id = ?", username, userId);
    }

    @Override
    public void updateImage(long userId, Long imageId) {
        jdbcTemplate.update("UPDATE users SET image_id = ? WHERE user_id = ?", imageId, userId);
    }

    @Override
    public void addFollow(long userId, long follows) {
        final Map<String, Object> followData = new HashMap<>();
        followData.put("user_id", userId);
        followData.put("follows", follows);

        followJdbcInsert.execute(followData);
    }
    @Override
    public void unfollow(long userId, long follows) {
        jdbcTemplate.update("DELETE FROM follows WHERE user_id = ? AND follows = ?", new Object[]{userId, follows});
    }

    @Override
    public boolean isFollowing(long userId, long followId) {
        return  jdbcTemplate.queryForObject("SELECT count(*) FROM follows WHERE user_id = ? AND follows = ?",
                new Object[]{userId, followId}, Integer.class) > 0;

    }

    @Override
    public Page<User> searchUsers(int page, String search) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("pageSize", USERS_PAGE_SIZE)
                .addValue("offset", (page - 1) * USERS_PAGE_SIZE)
                .addValue("search", "%" + search.toLowerCase() + "%");
        List<User> users = namedJdbcTemplate.query("SELECT * FROM users NATURAL LEFT JOIN user_positivity " +
                        "WHERE LOWER(username) LIKE :search OR LOWER(email) LIKE :search LIMIT :pageSize OFFSET :offset ",  params, ROW_MAPPER);

        int rowsCount = namedJdbcTemplate.query("SELECT count(*) AS userCount FROM users NATURAL LEFT JOIN user_positivity " +
                        "WHERE LOWER(username) LIKE :search OR LOWER(email) LIKE :search LIMIT :pageSize OFFSET :offset ",  params,
                ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / USERS_PAGE_SIZE);
        total =  total == 0 ? 1 : total;

        return new Page<>(users,page,total);
    }

    public List<User> getAll(int page){
        return jdbcTemplate.query("SELECT * FROM Users NATURAL LEFT JOIN user_positivity LIMIT 10 OFFSET ?", new Object[]{(page-1)*10},ROW_MAPPER);
    }

    @Override
    public List<User> getTopCreators(int qty) {
        return jdbcTemplate.query("WITH interactions AS (SELECT creator AS user_id, count(*) AS interaction_count FROM upvotes JOIN news ON upvotes.news_id = news.news_id " +
                "WHERE DATE(interaction_date) = CURRENT_DATE GROUP BY creator LIMIT ?) SELECT * FROM interactions NATURAL JOIN users NATURAL JOIN user_positivity ORDER BY interaction_count DESC", new Object[]{qty},ROW_MAPPER);

    }


}
