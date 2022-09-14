package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CategoryJdbcDao implements CategoryDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public boolean addCategoryToNews(long newsId, Category category) {
        String sql = "INSERT INTO news_category VALUES (?, ?) ON CONFLICT DO NOTHING";
        return jdbcTemplate.update(sql, category.ordinal(), newsId) == 1;
    }
}
