package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleJdbcDao implements RoleDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<String> ROLE_ROW_MAPPER = (rs, rowNum) -> rs.getString("user_role");

    @Autowired
    public RoleJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("user_role");
    }

    @Override
    public void addRole(long userId, Role role) {
        final Map<String, Object> roleData = new HashMap<>();
        roleData.put("user_role", role.getRole());
        roleData.put("user_id", userId);

        jdbcInsert.execute(roleData);
    }

    @Override
    public List<String> getRoles(long userId) {
        return  jdbcTemplate.query("SELECT * FROM user_role WHERE user_id = ?", new Object[]{userId}, ROLE_ROW_MAPPER);
    }
}
