package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminJdbcDao implements AdminDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final double PAGE_SIZE = 10.0;

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


    @Autowired
    public AdminJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("report");
    }

    @Override
    public void reportNews(long newsId, long userId, ReportReason reportReason) {
        final Map<String, Object> reportData = new HashMap<>();
        reportData.put("news_id", newsId);
        reportData.put("user_id", userId);
        reportData.put("report_date", LocalDateTime.now());
        reportData.put("reason", reportReason.getDescription());
        jdbcInsert.execute(reportData);
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page) {

        List<ReportedNews> rn = jdbcTemplate.query(
                "SELECT news_id, body, title, subtitle, creator, creation_date, accesses, news.image_id, email, username, pass,status, users.image_id as user_image_id, COUNT(report.user_id) AS report_count " +
                "FROM (report NATURAL JOIN news) JOIN users ON users.user_id=news.creator " +
                "GROUP BY news_id, body, title, subtitle, creator, creation_date, accesses, news.image_id, email, username, pass, status, users.image_id " +
                "ORDER BY report_count DESC LIMIT ? OFFSET ?", new Object[]{PAGE_SIZE, (page-1)*PAGE_SIZE}, REPORT_ROW_MAPPER);

        return new Page<>(rn,page,getTotalReportedNews());
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
        int rowsCount = jdbcTemplate.query("SELECT COUNT(*) as report_count FROM report group by news_id" , ROW_COUNT_MAPPER).stream().findFirst().get();
        int total = (int) Math.ceil(rowsCount/PAGE_SIZE);
        return total==0?1:total;
    }
}
































