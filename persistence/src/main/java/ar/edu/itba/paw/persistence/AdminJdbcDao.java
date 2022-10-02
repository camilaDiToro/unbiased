package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    private final SimpleJdbcInsert jdbcReportInsert;
    private final SimpleJdbcInsert jdbcAdminInsert;

    private static final double PAGE_SIZE = 5.0;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminJdbcDao.class);

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
        jdbcReportInsert = new SimpleJdbcInsert(ds).withTableName("report");
        jdbcAdminInsert = new SimpleJdbcInsert(ds).withTableName("user_role");
    }

    @Override
    public void reportNews(News news, Long loggedUser, ReportReason reportReason) {
        final Map<String, Object> reportData = new HashMap<>();
        reportData.put("news_id", news.getNewsId());
        reportData.put("user_id", loggedUser);
        reportData.put("report_date", LocalDateTime.now());
        reportData.put("reason", reportReason.getDescription());
        jdbcReportInsert.execute(reportData);
        LOGGER.debug("News {} with id {} reported. The reason is {}", news.getTitle(), news.getNewsId(), reportReason.getDescription());
    }
    @Override
    public boolean hasReported(News news, Long loggedUser) {
        if (loggedUser == null)
            return false;
        int rowsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) AS row_count FROM report WHERE user_id = ? AND news_id = ?", new Object[]{loggedUser, news.getNewsId()}, Integer.class);
        return rowsCount > 0;
    }

    @Override
    public void makeUserAdmin(User user) {
        int rowsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) as row_count FROM user_role WHERE user_id = ? AND user_role = ?", new Object[]{user.getId(), Role.ADMIN.getRole()},Integer.class);
        if(rowsCount!=0)
            return;
        final Map<String, Object> adminData = new HashMap<>();
        adminData.put("user_role", Role.ADMIN.getRole());
        adminData.put("user_id", user.getId());
        jdbcAdminInsert.execute(adminData);
        LOGGER.debug("User {} with id {} was made admin.", user.getEmail(), user.getId());
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page, ReportOrder reportOrder) {

        List<ReportedNews> rn = jdbcTemplate.query(
                "SELECT news_id, body, title, subtitle, creator, creation_date, accesses, news.image_id, email, username, " +
                "pass ,status , users.image_id as user_image_id, COUNT(report.user_id) AS report_count " +
                "FROM (report NATURAL JOIN news) JOIN users ON users.user_id=news.creator " +
                "GROUP BY news_id, body, title, subtitle, creator, creation_date, accesses, news.image_id, email, username, pass, status, users.image_id " +
                "ORDER BY "+ reportOrder.getQuery() +" LIMIT ? OFFSET ?", new Object[]{PAGE_SIZE, (page-1)*PAGE_SIZE}, REPORT_ROW_MAPPER);

        return new Page<>(rn,page,getTotalReportedNews());
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, News news) {
        List<ReportDetail> rd = jdbcTemplate.query(
                "SELECT * FROM report NATURAL JOIN users WHERE news_id = ? ORDER BY report_date DESC LIMIT ? OFFSET ?",
                     new Object[]{news.getNewsId(), PAGE_SIZE, (page-1)*PAGE_SIZE}, REPORT_DETAIL_ROW_MAPPER);

        return new Page<>(rd, page, getTotalReportsOfANews(news.getNewsId()));
    }


    private int getTotalReportsOfANews(long newsId){
        return JdbcUtils.getPageCount(jdbcTemplate.queryForObject("SELECT COUNT(*) as row_count FROM report WHERE news_id = ?" ,new Object[]{newsId}, Integer.class), PAGE_SIZE);
    }

    private int getTotalReportedNews() {
        return JdbcUtils.getPageCount(jdbcTemplate.queryForObject(" with grouped_reports as (SELECT news_id FROM report group by news_id) " +
                "SELECT count(*) AS row_count from grouped_reports" , Integer.class), PAGE_SIZE);
    }

}
































