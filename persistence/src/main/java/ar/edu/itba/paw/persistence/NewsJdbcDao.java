package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
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
    private final SimpleJdbcInsert jdbcUpvoteInsert;

    private final CategoryDao categoryDao;

    private static final double PAGE_SIZE = 9.0;

    private static final RowMapper<News> NEWS_ROW_MAPPER = (rs, rowNum) ->
            new News.NewsBuilder(   rs.getLong("creator"),
                                    rs.getString("body"),
                                    rs.getString("title"),
                                    rs.getString("subtitle"))
                                    .newsId(rs.getLong("news_id"))
                                    .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
                                    .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                                    .build();

    private static final RowMapper<Category> CATEGORIES_ROW_MAPPER = (rs, rowNum) ->
            Category.getById(rs.getLong("category_id"));

    private final static RowMapper<Integer> ROW_COUNT_MAPPER = (rs, rowNum) -> rs.getInt("newsCount");

    private final static RowMapper<Integer> UPVOTES_MAPPER = (rs, rowNum) -> rs.getInt("upvotes");
    private final static RowMapper<Boolean> RATING_MAPPER = (rs, rowNum) -> rs.getBoolean("upvote");
    private final static RowMapper<Double> INTERACTIONS_MAPPER = (rs, rowNum) -> rs.getDouble("interactions");




    @Autowired
    public NewsJdbcDao(final DataSource dataSource, final CategoryDao categoryDao){
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("news").usingGeneratedKeyColumns("news_id");
        jdbcUpvoteInsert = new SimpleJdbcInsert(dataSource).withTableName("upvotes");

        this.categoryDao = categoryDao;
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
        newsData.put("accesses", 0);

        final long newsId = jdbcInsert.executeAndReturnKey(newsData).longValue();

        for(Category c : newsBuilder.getCategories()){
            categoryDao.addCategoryToNews(newsId, c);
        }

        return newsBuilder.newsId(newsId).build();
    }

    @Override
    public List<News> getNews(int page, NewsOrder ns) {
        return jdbcTemplate.query("SELECT * FROM news ORDER BY " +  ns.getQuery() + " LIMIT ? OFFSET ?", new Object[]{PAGE_SIZE, (page-1)*PAGE_SIZE},NEWS_ROW_MAPPER);
    }

    @Override
    public int getTotalPagesAllNews() {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news" , ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount/PAGE_SIZE);
        return total==0?1:total;
    }

    @Override
    public List<News> getNews(int page, String query, NewsOrder ns) {
        List<News> news =  jdbcTemplate.query("SELECT * FROM news WHERE LOWER(title) LIKE ? ORDER BY " +  ns.getQuery() + " LIMIT ? OFFSET ? ",
                new Object[]{"%" + query.toLowerCase() + "%", PAGE_SIZE, (page-1)*PAGE_SIZE},NEWS_ROW_MAPPER);
        return news;
    }



    @Override
    public int getTotalPagesAllNews(String query) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news WHERE LOWER(title) LIKE ?" ,
                new Object[]{"%" + query.toLowerCase() + "%"},ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount/PAGE_SIZE);
        return total==0?1:total;
    }


    @Override
    public Optional<News> getById(long id) {
        Optional<News> news =  jdbcTemplate.query("SELECT * FROM news WHERE news_id = ?",
                new Object[] { id }, NEWS_ROW_MAPPER).stream().findFirst();
        jdbcTemplate.update("UPDATE news SET accesses = accesses + 1 WHERE news_id = ?",id);
        return news;
    }

    @Override
    public List<News> getNewsByCategory(int page, Category category, NewsOrder ns) {
        return jdbcTemplate.query("SELECT * FROM news NATURAL JOIN news_category WHERE category_id = ? ORDER BY " +  ns.getQuery() + " LIMIT ? OFFSET ? ",
                new Object[]{category.getId(), PAGE_SIZE, (page-1)*PAGE_SIZE},NEWS_ROW_MAPPER);
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        List<Category> categories =  jdbcTemplate.query("SELECT category_id FROM news NATURAL JOIN news_category WHERE news_id = ?",
                new Object[]{news.getNewsId()},CATEGORIES_ROW_MAPPER);
        return categories;
    }

    @Override
    public int getTotalPagesCategory(Category category) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news NATURAL JOIN news_category WHERE category_id = ?" ,
                new Object[]{category.getId()},ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount/PAGE_SIZE);
        return total==0?1:total;
    }

@Override
    public int getUpvotes(Long newsId) {
        int upvotes = jdbcTemplate.query("SELECT sum(case when upvote=true then 1 else -1 end) AS upvotes FROM upvotes where news_id = ?",
                new Object[]{newsId},UPVOTES_MAPPER).stream().findFirst().get();
        return upvotes;
    }
    @Override
    public Rating upvoteState(News news, User user) {
        Optional<Boolean> rating = jdbcTemplate.query("SELECT upvote AS upvote FROM upvotes WHERE user_id = ? AND news_id = ?",
                new Object[]{user.getId(), news.getNewsId()},RATING_MAPPER).stream().findFirst();
        return rating.map(aBoolean -> aBoolean ? Rating.UPVOTE : Rating.DOWNVOTE).orElse(Rating.NO_RATING);
    }
    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {
        jdbcTemplate.update("DELETE FROM upvotes WHERE user_id = ? AND news_id = ?",
                new Object[]{userId, newsId});
        if (rating.equals(Rating.NO_RATING))
            return;

        final Map<String,Object> ratingData = new HashMap<>();
        ratingData.put("news_id",newsId);
        ratingData.put("user_id", userId);
        ratingData.put("upvote",rating.equals(Rating.UPVOTE));

        jdbcUpvoteInsert.execute(ratingData);

    }

    @Override
    public double getPositivityValue(Long newsId) {
       int upvotes = jdbcTemplate.query("(SELECT sum(case when upvote=true then 1 else 0 end) AS upvotes FROM upvotes WHERE news_id = ?)",
               new Object[]{newsId}, UPVOTES_MAPPER).stream().findFirst().get();
       double interactions = jdbcTemplate.query("(SELECT count(*) AS interactions FROM upvotes WHERE news_id = ?)",
               new Object[]{newsId}, INTERACTIONS_MAPPER).stream().findFirst().get();

       return interactions == 0 ? 1 : upvotes / interactions;
    }
}
