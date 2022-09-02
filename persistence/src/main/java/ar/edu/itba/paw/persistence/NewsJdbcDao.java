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

    private static final int PAGE_SIZE = 9;

    private static final RowMapper<News> NEWS_ROW_MAPPER = (rs, rowNum) ->
            new News.NewsBuilder(   rs.getLong("creator"),
                                    rs.getString("body"),
                                    rs.getString("title"),
                                    rs.getString("subtitle"))
                                    .newsId(rs.getLong("news_id"))
                                    .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
                                    .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                                    .build();

    private final static RowMapper<Integer> ROW_COUNT_MAPPER = (rs, rowNum) -> rs.getInt("newsCount");

    @Autowired
    public NewsJdbcDao(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("news").usingGeneratedKeyColumns("news_id");
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
    public List<News> getNews(int page) {
        return jdbcTemplate.query("SELECT * FROM news LIMIT ? OFFSET ?", new Object[]{PAGE_SIZE, (page-1)*PAGE_SIZE},NEWS_ROW_MAPPER);
    }

    @Override
    public int getTotalPagesAllNews() {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news" , ROW_COUNT_MAPPER).stream().findFirst().get();
        return rowsCount/PAGE_SIZE + 1;
    }

    @Override
    public List<News> getNews(int page, String query) {
        List<News> news =  jdbcTemplate.query("SELECT * FROM news WHERE LOWER(title) LIKE ? LIMIT ? OFFSET ? ", new Object[]{"%" + query.toLowerCase() + "%", PAGE_SIZE, (page-1)*PAGE_SIZE},NEWS_ROW_MAPPER);
//        List<News> result = new ArrayList<>();
//        for (News article : news) {
//            Set<String> fields = new HashSet<>(Arrays.asList(article.getBody(), article.getTitle(), article.getSubtitle()));
//            for (String field : fields) {
//                if (field.toLowerCase().contains(query.toLowerCase())) {
//                    result.add(article);
//                    break;
//                }
//            }
//        }

        return news;
    }

    @Override
    public int getTotalPagesAllNews(String query) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news WHERE LOWER(title) LIKE ?" , new Object[]{"%" + query.toLowerCase() + "%"},ROW_COUNT_MAPPER).stream().findFirst().get();
        return rowsCount/PAGE_SIZE + 1;
    }


    @Override
    public Optional<News> getById(long id) {
        return jdbcTemplate.query("SELECT * FROM news WHERE news_id = ?",
                new Object[] { id }, NEWS_ROW_MAPPER).stream().findFirst();
    }
}
