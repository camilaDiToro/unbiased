package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;


@Repository
public class NewsJdbcDao implements NewsDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final UserDao userDao;

    private static final RowMapper<News> NEWS_ROW_MAPPER = (rs, rowNum) ->
            new News.NewsBuilder(   rs.getLong("creator"),
                                    rs.getString("body"),
                                    rs.getString("title"),
                                    rs.getString("subtitle"))
                                    .newsId(rs.getLong("news_id"))
                                    .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
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
        newsData.put("creator", newsBuilder.getCreatorId());
        newsData.put("creation_date",newsBuilder.getCreationDate());
        newsData.put("image_id", newsBuilder.getImageId());

        final long newsId = jdbcInsert.executeAndReturnKey(newsData).longValue();
        return newsBuilder.newsId(newsId).build();
    }

    @Override
    public List<News> getNews() {
//        List<News> news = new ArrayList<>();
//        news.add(new News.NewsBuilder(new User(0L,"", 0L),  "BODY", "Title 1", "Subtitle 1").build());
//        news.add(new News.NewsBuilder(new User(0L,"", 0L),  "This is a short card.", "Card title", "Subtitle 2").build());
//        news.add(new News.NewsBuilder(new User(0L,"", 0L),  "This is a longer card with supporting text below as a natural lead-in to additional content.", "Card title 3", "Subtitle 3").build());
//        news.add(new News.NewsBuilder(new User(0L,"", 0L),  "This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.", "Card title 4", "Subtitle 4").build());

        return jdbcTemplate.query("SELECT * FROM news",NEWS_ROW_MAPPER);
    }

    @Override
    public Optional<News> getById(long id) {
        return jdbcTemplate.query("SELECT * FROM news WHERE news_id = ?",
                new Object[] { id }, NEWS_ROW_MAPPER).stream().findFirst();
    }
}
