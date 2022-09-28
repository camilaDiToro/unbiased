package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.LoggedUserParameters;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NewsJdbcDao implements NewsDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcUpvoteInsert;
    private final SimpleJdbcInsert jdbcSavedNewsInsert;
    private final SimpleJdbcInsert jdbcCommentsInsert;

    private final CategoryDao categoryDao;

    private static final double PAGE_SIZE = 10.0;
    private static final double COMMENT_PAGE_SIZE = 5.0;
    private static final double PROFILE_PAGE_SIZE = 5.0;
    private static final double RECOMMENDATION_PAGE_SIZE = 10.0;


    private static final RowMapper<FullNews> FULLNEWS_ROW_MAPPER = (rs, rowNum) -> {
        News news = new News.NewsBuilder(rs.getLong("creator"),
                rs.getString("body"),
                rs.getString("title"),
                rs.getString("subtitle"))
                .newsId(rs.getLong("news_id"))
                .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
                .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                .build();
//        rs.getObject("news_id")
        long userImageId = rs.getLong("user_image_id");
        User creator = new User.UserBuilder(rs.getString("email"))
                .username(rs.getString("username"))
                .userId(rs.getLong("creator"))
                .pass(rs.getString("pass"))
                .imageId(userImageId == 0 ? null : userImageId)
                .status(rs.getString("status")).build();


        PositivityStats stats = new PositivityStats(rs.getInt("upvotes"), rs.getInt("downvotes"));
        LoggedUserParameters loggedParams = null;
        if (rs.getObject("logged_user") != null) {
            Rating rating = Rating.getRating((Boolean) rs.getObject("upvote"));
            boolean saved = rs.getObject("saved_date") != null;


            loggedParams = new LoggedUserParameters(rating, saved);
        }
        return new FullNews(news, creator, stats, loggedParams);
    };

    private static final RowMapper<News> NEWS_ROW_MAPPER = (rs, rowNum) ->
            new News.NewsBuilder(   rs.getLong("creator"),
                    rs.getString("body"),
                    rs.getString("title"),
                    rs.getString("subtitle"))
                    .newsId(rs.getLong("news_id"))
                    .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
                    .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                    .build();

    private static final RowMapper<Comment> COMMENTS_ROW_MAPPER = (rs, rowNum) -> {
        long userImageId = rs.getLong("image_id");
        User creator = new User.UserBuilder(rs.getString("email"))
                .username(rs.getString("username"))
                .userId(rs.getLong("user_id"))
                .pass(rs.getString("pass"))
                .imageId(userImageId == 0 ? null : userImageId)
                .status(rs.getString("status")).build();


        return new Comment(creator, rs.getString("comment"), rs.getTimestamp("commented_date").toLocalDateTime());
    };


    private static final RowMapper<Category> CATEGORIES_ROW_MAPPER = (rs, rowNum) ->
            Category.getById(rs.getLong("category_id"));

    private final static RowMapper<Integer> ROW_COUNT_MAPPER = (rs, rowNum) -> rs.getInt("newsCount");

    private final static RowMapper<PositivityStats> NEWS_STATS_ROW_MAPPER = (rs, rowNum) -> new PositivityStats(rs.getInt("upvotes"), rs.getInt("downvotes"));
    private final static RowMapper<Boolean> RATING_MAPPER = (rs, rowNum) -> rs.getBoolean("upvote");
    private final static RowMapper<Integer> UPVOTES_MAPPER = (rs, rowNum) -> rs.getInt("upvotes");


    @Autowired
    public NewsJdbcDao(final DataSource dataSource, final CategoryDao categoryDao) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("news").usingGeneratedKeyColumns("news_id");
        jdbcUpvoteInsert = new SimpleJdbcInsert(dataSource).withTableName("upvotes");
        jdbcSavedNewsInsert = new SimpleJdbcInsert(dataSource).withTableName("saved_news");
        jdbcCommentsInsert = new SimpleJdbcInsert(dataSource).withTableName("comments");

        this.categoryDao = categoryDao;
    }

    @Override
    public News create(News.NewsBuilder newsBuilder) {

        final Map<String, Object> newsData = new HashMap<>();
        newsData.put("body", newsBuilder.getBody());
        newsData.put("title", newsBuilder.getTitle());
        newsData.put("subtitle", newsBuilder.getSubtitle());
        newsData.put("creator", newsBuilder.getCreatorId());
        newsData.put("creation_date", Timestamp.valueOf(newsBuilder.getCreationDate()));
        newsData.put("image_id", newsBuilder.getImageId());
        newsData.put("accesses", 0);

        final long newsId = jdbcInsert.executeAndReturnKey(newsData).longValue();

        for (Category c : newsBuilder.getCategories()) {
            categoryDao.addCategoryToNews(newsId, c);
        }

        return newsBuilder.newsId(newsId).build();
    }

    @Override
    public int getTotalPagesAllNews() {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news", ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / PAGE_SIZE);
        return total == 0 ? 1 : total;
    }

    @Override
    public List<FullNews> getNews(int page, String query, NewsOrder ns, Long loggedUser) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("pageSize", PAGE_SIZE)
                .addValue("offset", (page - 1) * PAGE_SIZE)
                .addValue("loggedId", loggedUser)
                .addValue("query", "%" + query.toLowerCase() + "%");
        if (loggedUser != null) {
            return namedJdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :loggedId) " +
                            "SELECT full_news.*, upvote, saved_date, :loggedId AS logged_user FROM full_news NATURAL LEFT JOIN logged_params " +
                            "WHERE LOWER(title) LIKE :query ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                    params, FULLNEWS_ROW_MAPPER);

        }
        return namedJdbcTemplate.query("SELECT *,null as logged_user FROM full_news WHERE LOWER(title) LIKE :query ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                params, FULLNEWS_ROW_MAPPER);
    }

    @Override
    public int getTotalPagesAllNews(String query) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news WHERE LOWER(title) LIKE ?",
                new Object[]{"%" + query.toLowerCase() + "%"}, ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / PAGE_SIZE);
        return total == 0 ? 1 : total;
    }

    @Override
    public Optional<FullNews> getById(long id, Long loggedUser) {
        Optional<FullNews> news;
        if (loggedUser != null) {
            news = jdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = ?) " +
                            "SELECT full_news.*, upvote, saved_date, ? AS logged_user FROM full_news NATURAL LEFT JOIN logged_params WHERE news_id = ?",
                    new Object[]{loggedUser, loggedUser, id}, FULLNEWS_ROW_MAPPER).stream().findFirst();

        } else {
            news = jdbcTemplate.query(
                    "SELECT *, null as logged_user FROM full_news  WHERE news_id = ?",
                    new Object[]{id}, FULLNEWS_ROW_MAPPER).stream().findFirst();
        }

        jdbcTemplate.update("UPDATE news SET accesses = accesses + 1 WHERE news_id = ?", id);
        return news;

    }

    @Override
    public Optional<News> getSimpleNewsById(long id) {
        return jdbcTemplate.query("SELECT * FROM news WHERE news_id = ?",
                new Object[] { id }, NEWS_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<FullNews> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("pageSize", PAGE_SIZE)
                .addValue("offset", (page - 1) * PAGE_SIZE)
                .addValue("loggedId", loggedUser)
                .addValue("category", category.getId());
        if (loggedUser != null) {
            return namedJdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :loggedId) " +
                            "SELECT full_news.*, upvote, saved_date, :loggedId AS logged_user FROM full_news NATURAL JOIN news_category NATURAL LEFT JOIN logged_params WHERE category_id = :category" +
                            " ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                    params, FULLNEWS_ROW_MAPPER);
        }
        return namedJdbcTemplate.query("SELECT *, null as logged_user FROM full_news NATURAL JOIN news_category WHERE category_id = :category ORDER BY " + ns.getQuery() +
                        " LIMIT :pageSize OFFSET :offset ",
                params, FULLNEWS_ROW_MAPPER);
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        List<Category> categories = jdbcTemplate.query("SELECT category_id FROM news NATURAL JOIN news_category WHERE news_id = ?",
                new Object[]{news.getNewsId()}, CATEGORIES_ROW_MAPPER);
        return categories;
    }

    @Override
    public List<FullNews> getAllNewsFromUser(int page, User user, NewsOrder ns, Long loggedUser) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("creatorId", user.getId())
                .addValue("pageSize", PROFILE_PAGE_SIZE)
                .addValue("offset", (page - 1) * PROFILE_PAGE_SIZE)
                .addValue("loggedId", loggedUser);
        if (loggedUser != null) {
            return namedJdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :loggedId) " +
                            "SELECT full_news.*, upvote, saved_date, :loggedId AS logged_user FROM full_news NATURAL LEFT JOIN logged_params" +
                            " WHERE creator = :creatorId ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                    params, FULLNEWS_ROW_MAPPER);
        }
        return namedJdbcTemplate.query("SELECT *, null as logged_user FROM full_news WHERE creator = :creatorId ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                params, FULLNEWS_ROW_MAPPER);

    }


    @Override
    public List<FullNews> getSavedNews(int page, long userId, NewsOrder ns, Long loggedUser) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("pageSize", PROFILE_PAGE_SIZE)
                .addValue("offset", (page - 1) * PROFILE_PAGE_SIZE);


        if (loggedUser != null) {
            params.addValue("loggedId", (long) loggedUser);
            return namedJdbcTemplate.query(
                    "WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :loggedId) " +
                            "SELECT full_news.*, upvote, saved_date, :loggedId AS logged_user FROM saved_news NATURAL JOIN full_news NATURAL LEFT JOIN logged_params " +
                            " WHERE user_id = :userId  " +
                            "ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                    params, FULLNEWS_ROW_MAPPER);
        }

        return namedJdbcTemplate.query("SELECT *, null as logged_user FROM saved_news NATURAL JOIN full_news " +
                        "    WHERE user_id = :userId " +
                        "ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                params, FULLNEWS_ROW_MAPPER);

    }

    private List<FullNews> getNewsWithRatingFromUser(int page, long userId, NewsOrder ns, Long loggedUser, boolean upvote) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("pageSize", PROFILE_PAGE_SIZE)
                .addValue("offset", (page - 1) * PROFILE_PAGE_SIZE)
                .addValue("loggedId", loggedUser)
                .addValue("upvote", upvote);


        if (loggedUser != null) {
            return namedJdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :loggedId) " +
                            "SELECT full_news.*, upvote, saved_date, :loggedId AS logged_user FROM upvotes NATURAL JOIN full_news NATURAL LEFT JOIN logged_params " +
                            " WHERE user_id = :userId AND upvote = :upvote " +
                            "ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                    params, FULLNEWS_ROW_MAPPER);
        }

        return namedJdbcTemplate.query("SELECT *, null as logged_user FROM upvotes NATURAL RIGHT JOIN full_news " +
                        "    WHERE user_id = :userId AND upvote = :upvote " +
                        "ORDER BY " + ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                params, FULLNEWS_ROW_MAPPER);
    }

    @Override
    public List<FullNews> getNewsUpvotedByUser(int page, User user, NewsOrder ns, Long loggedUser) {
        return getNewsWithRatingFromUser(page, user.getId(), ns, loggedUser, true);
    }

    @Override
    public List<FullNews> getNewsDownvotedByUser(int page, User user, NewsOrder ns, Long loggedUser) {
        return getNewsWithRatingFromUser(page, user.getId(), ns, loggedUser, false);
    }

    @Override
    public int getTotalPagesNewsFromUser(int page, User user) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news WHERE creator = ?",
                new Object[]{user.getId()}, ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / PROFILE_PAGE_SIZE);
        return total == 0 ? 1 : total;
    }

    private int getTotalPagesNewsFromUserRating(int page, long userId, boolean upvoted) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news NATURAL JOIN upvotes WHERE user_id = ? AND upvote = ?",
                new Object[]{userId, upvoted}, ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / PROFILE_PAGE_SIZE);
        return total == 0 ? 1 : total;
    }

    @Override
    public int getTotalPagesNewsFromUserUpvoted(int page, User user) {
        return getTotalPagesNewsFromUserRating(page, user.getId(), true);
    }

    @Override
    public int getTotalPagesNewsFromUserDownvoted(int page, User user) {
        return getTotalPagesNewsFromUserRating(page, user.getId(), false);
    }

    @Override
    public int getTotalPagesNewsFromUserSaved(int page, User user) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news NATURAL JOIN saved_news WHERE user_id = ?",
                new Object[]{user.getId()}, ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / PROFILE_PAGE_SIZE);
        return total == 0 ? 1 : total;
    }

    @Override
    public void deleteNews(News news) {
        jdbcTemplate.update("DELETE FROM news WHERE news_id = ?", news.getNewsId());
    }

    @Override
    public int getTotalPagesCategory(Category category) {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news NATURAL JOIN news_category WHERE category_id = ?",
                new Object[]{category.getId()}, ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount / PAGE_SIZE);
        return total == 0 ? 1 : total;
    }


    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {
        jdbcTemplate.update("DELETE FROM upvotes WHERE user_id = ? AND news_id = ?",
                new Object[]{userId, newsId});
        if (rating.equals(Rating.NO_RATING))
            return;

        final Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("news_id", newsId);
        ratingData.put("user_id", userId);
        ratingData.put("upvote", rating.equals(Rating.UPVOTE));
        ratingData.put("interaction_date", Timestamp.valueOf(LocalDateTime.now()));

        jdbcUpvoteInsert.execute(ratingData);
    }

    @Override
    public void saveNews(News news, User user) {
        final Map<String,Object> savedNewsData = new HashMap<>();
        savedNewsData.put("news_id",news.getNewsId());
        savedNewsData.put("user_id", user.getId());
        savedNewsData.put("saved_date", Timestamp.valueOf(LocalDateTime.now()));

        jdbcSavedNewsInsert.execute(savedNewsData);
    }


    private int getTotalPagesComments(long newsId) {
        int rowsCount = jdbcTemplate.queryForObject("SELECT count(*) FROM comments NATURAL JOIN users WHERE news_id = ?",
                new Object[]{newsId}, Integer.class);
        int total = (int) Math.ceil(rowsCount / COMMENT_PAGE_SIZE);
        return total == 0 ? 1 : total;
    }

    @Override
    public void addComment(User user, News news, String comment) {
        final Map<String, Object> commentData = new HashMap<>();
        commentData.put("news_id", news.getNewsId());
        commentData.put("user_id", user.getId());
        commentData.put("comment", comment);

        commentData.put("commented_date", Timestamp.valueOf(LocalDateTime.now()));

        jdbcCommentsInsert.execute(commentData);

    }

    @Override
    public Page<Comment> getComments(long newsId, int page) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("newsId", newsId)
                .addValue("pageSize", COMMENT_PAGE_SIZE)
                .addValue("offset", (page - 1) * COMMENT_PAGE_SIZE);


        List<Comment> comments = namedJdbcTemplate.query("SELECT * FROM comments NATURAL JOIN users " +
                        " WHERE news_id = :newsId " +
                        " LIMIT :pageSize OFFSET :offset ",
                params, COMMENTS_ROW_MAPPER).stream().collect(Collectors.toList());

        return new Page<>(comments, page, getTotalPagesComments(newsId));
    }

    @Override
    public void removeSaved(News news, User user) {
        jdbcTemplate.update("DELETE FROM saved_news WHERE news_id = ? AND user_id = ?",new Object[]{news.getNewsId(), user.getId()});
    }


    @Override
    public List<FullNews> getRecommendation(int page, User user, NewsOrder newsOrder) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pageSize", RECOMMENDATION_PAGE_SIZE)
                .addValue("offset", (page-1)*RECOMMENDATION_PAGE_SIZE)
                .addValue("userId", user.getId())
                .addValue("date", Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        List<FullNews> fullNews = namedJdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :userId) " +
                        "SELECT full_news.*, upvote, saved_date, :userId AS logged_user, 1 as priority FROM full_news JOIN follows " +
                        "ON (:userId = follows.user_id AND follows.follows=full_news.creator ) " +
                        "NATURAL LEFT JOIN logged_params WHERE full_news.creation_date >= :date " +
                        "UNION " +
                        "(SELECT full_news.*, upvote, saved_date, :userId AS logged_user, 2 as priority FROM full_news " +
                        "NATURAL LEFT JOIN logged_params WHERE full_news.creation_date >= :date " +
                        "AND creator IN (SELECT creator FROM news NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)"  +
                        "AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)) " +
                        "UNION " +
                        "(SELECT full_news.*, upvote, saved_date, :userId AS logged_user, 3 as priority FROM full_news " +
                        "NATURAL LEFT JOIN logged_params WHERE full_news.creation_date >= :date " +
                        "AND creator NOT IN (SELECT creator FROM news NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId) " +
                        "AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId))" +
                        "ORDER BY priority, " + newsOrder.getQuery() + " LIMIT :pageSize OFFSET :offset ",
                params, FULLNEWS_ROW_MAPPER);
        return fullNews;
    }

    @Override
    public int getTodayNewsPageCount() {
        int rowsCount = jdbcTemplate.query("SELECT count(*) AS newsCount FROM news WHERE creation_date >= ?" ,
                new Object[]{Timestamp.valueOf(LocalDateTime.now().minusDays(1))},ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount/RECOMMENDATION_PAGE_SIZE);
        return total==0?1:total;
    }

}
