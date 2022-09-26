package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.PositivityStats;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.LoggedUserParameters;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminJdbcDao implements AdminDao{

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final SimpleJdbcInsert jdbcReportInsert;
    private final SimpleJdbcInsert jdbcAdminInsert;

    private static final double PAGE_SIZE = 5.0;

    private static final RowMapper<Integer> ROW_COUNT_MAPPER = (rs, rowNum) -> rs.getInt("report_count");
    private static final RowMapper<ReportedNews> REPORT_ROW_MAPPER = (rs, rowNum) ->
            new ReportedNews(   new News.NewsBuilder(rs.getLong("creator"),rs.getString("body"),rs.getString("title"),rs.getString("subtitle"))
                                                .newsId(rs.getLong("news_id"))
                                                .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
                                                .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                                                .build(),
                                new User.UserBuilder(rs.getString("email"))
                                                .username(rs.getString("username"))
                                                .userId(rs.getLong("creator"))
                                                .pass(rs.getString("pass"))
                                                .imageId(rs.getLong("user_image_id") == 0 ? null : rs.getLong("user_image_id"))
                                                .status(rs.getString("status")).build(),
                                rs.getInt("report_count") );
    private static final RowMapper<ReportDetail> REPORT_DETAIL_ROW_MAPPER = (rs, rowNum) ->
            new ReportDetail(   new User.UserBuilder(rs.getString("email"))
                                                .username(rs.getString("username"))
                                                .userId(rs.getLong("user_id"))
                                                .pass(rs.getString("pass"))
                                                .imageId(rs.getLong("image_id") == 0 ? null : rs.getLong("image_id"))
                                                .status(rs.getString("status")).build(),
                                rs.getTimestamp("report_date").toLocalDateTime(),
                                rs.getString("reason")
            );

//    private static final RowMapper<ReportedNews> REPORT_ROW_MAPPER = (rs, rowNum) -> {
//        News news = new News.NewsBuilder(   rs.getLong("creator"),
//                rs.getString("body"),
//                rs.getString("title"),
//                rs.getString("subtitle"))
//                .newsId(rs.getLong("news_id"))
//                .imageId(rs.getObject("image_id") == null ? null : rs.getLong("image_id"))
//                .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
//                .build();
////        rs.getObject("news_id")
//        long userImageId = rs.getLong("user_image_id");
//        User creator = new User.UserBuilder(rs.getString("email"))
//                .username(rs.getString("username"))
//                .userId(rs.getLong("creator"))
//                .pass(rs.getString("pass"))
//                .imageId(userImageId == 0 ? null : userImageId)
//                .status(rs.getString("status")).build();
//
//
//
//        PositivityStats stats = new PositivityStats(rs.getInt("upvotes"), rs.getInt("downvotes"));
//        LoggedUserParameters loggedParams = null;
//        if (rs.getObject("logged_user") != null) {
//            Rating rating = Rating.getRating((Boolean) rs.getObject("upvote"));
//            boolean saved = rs.getObject("saved_date") != null;
//
//
//            loggedParams = new LoggedUserParameters(rating, saved);
//        }
//        return new ReportedNews(news, creator, stats,  rs.getInt("report_count"), loggedParams);
//    };


    @Autowired
    public AdminJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);

        jdbcReportInsert = new SimpleJdbcInsert(ds).withTableName("report");
        jdbcAdminInsert = new SimpleJdbcInsert(ds).withTableName("user_role");
    }

    @Override
    public void reportNews(long newsId, Long loggedUser, ReportReason reportReason) {
//        if (loggedUser == null)
//            throw new ;
        final Map<String, Object> reportData = new HashMap<>();
        reportData.put("news_id", newsId);
        reportData.put("user_id", loggedUser);
        reportData.put("report_date", Timestamp.valueOf(LocalDateTime.now()));
        reportData.put("reason", reportReason.getDescription());
        jdbcReportInsert.execute(reportData);
    }
    @Override
    public boolean hasReported(long newsId, Long loggedUser) {
        if (loggedUser == null)
            return false;
        int rowsCount = jdbcTemplate.query("SELECT COUNT(*) AS report_count FROM report WHERE user_id = ? AND news_id = ?", new Object[]{loggedUser, newsId}, ROW_COUNT_MAPPER).stream().findFirst().orElse(0);
        return rowsCount > 0;
    }

    @Override
    public void makeUserAdmin(long userId) {
        int rowsCount = jdbcTemplate.query("SELECT COUNT(*) as report_count FROM user_role WHERE user_id = ? AND user_role = ?", new Object[]{userId, Role.ADMIN.getRole()}, ROW_COUNT_MAPPER).stream().findFirst().orElse(0);
        if(rowsCount!=0)
            return;
        final Map<String, Object> adminData = new HashMap<>();
        adminData.put("user_role", Role.ADMIN.getRole());
        adminData.put("user_id", userId);
        jdbcAdminInsert.execute(adminData);
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page, NewsOrder ns) {

        List<ReportedNews> rn = jdbcTemplate.query(
                "SELECT news_id, body, title, subtitle, creator, creation_date, accesses, news.image_id, email, username, pass,status, users.image_id as user_image_id, COUNT(report.user_id) AS report_count " +
                "FROM (report NATURAL JOIN news) JOIN users ON users.user_id=news.creator " +
                "GROUP BY news_id, body, title, subtitle, creator, creation_date, accesses, news.image_id, email, username, pass, status, users.image_id " +
                "ORDER BY report_count DESC LIMIT ? OFFSET ?", new Object[]{PAGE_SIZE, (page-1)*PAGE_SIZE}, REPORT_ROW_MAPPER);

        return new Page<>(rn,page,getTotalReportedNews());

//        List<ReportedNews> rn;
//
//        SqlParameterSource params = new MapSqlParameterSource()
//                .addValue("pageSize", PAGE_SIZE)
//                .addValue("offset", (page-1)*PAGE_SIZE)
//                .addValue("loggedId", loggedUser);
//        if(loggedUser != null) {
//            rn =  namedJdbcTemplate.query("WITH logged_params AS (SELECT * FROM logged_news_parameters WHERE logged_user = :loggedId) " +
//                            "SELECT count(report.user_id) as report_count, full_news.*, upvote, saved_date, :loggedId AS logged_user FROM full_news NATURAL JOIN report NATURAL LEFT JOIN logged_params " +
//                            "GROUP BY news_id" +
//                            " ORDER BY " +  ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
//                    params,REPORT_ROW_MAPPER);
//        }
//        else {
//            rn = namedJdbcTemplate.query("SELECT null as logged_user, count(report.user_id) as report_count, full_news.*, upvote, saved_date, :loggedId AS logged_user FROM full_news NATURAL JOIN report NATURAL LEFT JOIN logged_params " +
//                            "GROUP BY news_id" +
//                            " ORDER BY " +  ns.getQuery() + " LIMIT :pageSize OFFSET :offset ",
//                    params,REPORT_ROW_MAPPER);
//        }
//
//        return new Page<>(rn,page,getTotalReportedNews());
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, long newsId) {

        List<ReportDetail> rd = jdbcTemplate.query("SELECT * FROM report NATURAL JOIN users WHERE news_id = ? ORDER BY report_date DESC LIMIT ? OFFSET ?",
                                                    new Object[]{newsId, PAGE_SIZE, (page-1)*PAGE_SIZE}, REPORT_DETAIL_ROW_MAPPER);

        return new Page<>(rd, page, getTotalReportsOfANews(newsId));
    }


    private int getTotalReportsOfANews(long newsId){
        int rowsCount = jdbcTemplate.query("SELECT COUNT(*) as report_count FROM report WHERE news_id = ?" ,new Object[]{newsId}, ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount/PAGE_SIZE);
        return total==0?1:total;
    }

    private int getTotalReportedNews() {
        int rowsCount = jdbcTemplate.query(" with grouped_reports as (SELECT news_id FROM report group by news_id)\n" +
                "SELECT count(*) AS report_count from grouped_reports" , ROW_COUNT_MAPPER).stream().findFirst().orElse(0);
        int total = (int) Math.ceil(rowsCount/PAGE_SIZE);
        return total==0?1:total;
    }
}
































