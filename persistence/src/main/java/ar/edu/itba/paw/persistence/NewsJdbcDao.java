package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
public class NewsJdbcDao implements NewsDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final UserDao userDao;

    private static final RowMapper<News> NEWS_ROW_MAPPER = (rs, rowNum) ->
            new News.NewsBuilder(   new User(rs.getLong("creator"), rs.getString("email"), rs.getLong("data_id")),
                                    rs.getString("body"),
                                    rs.getString("title"),
                                    rs.getString("subtitle"))
                                    .newsId(rs.getLong("news_id"))
                                    .image(rs.getBytes("image"))
                                    .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                                    .build();


    @Autowired
    public NewsJdbcDao(final DataSource dataSource, final UserDao userDao){
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("news").usingGeneratedKeyColumns("news_id");
        this.userDao = userDao;
    }

    @Override
    public News create(News.NewsBuilder newsBuilder) {

        final Map<String,Object> newsData = new HashMap<>();
        newsData.put("body",newsBuilder.getBody());
        newsData.put("title", newsBuilder.getTitle());
        newsData.put("subtitle",newsBuilder.getSubtitle());
        newsData.put("creator", newsBuilder.getCreator().getId());
        newsData.put("creation_date",newsBuilder.getCreationDate());
        newsData.put("image", newsBuilder.getImage());

        final long newsId = jdbcInsert.executeAndReturnKey(newsData).longValue();
        return newsBuilder.newsId(newsId).build();
    }

    @Override
    public Optional<News> getById(long id) {
        return jdbcTemplate.query("SELECT * FROM ((users LEFT OUTER JOIN user_data on user_data.data_id = users.data_id) JOIN news ON user_id = creator) WHERE news_id = ?",
                new Object[] { id }, NEWS_ROW_MAPPER).stream().findFirst();
    }
}
