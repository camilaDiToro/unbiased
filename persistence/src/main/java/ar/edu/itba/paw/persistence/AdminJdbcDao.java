package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AdminJdbcDao implements AdminDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

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
        reportData.put("report_date", newsId);
        reportData.put("reason", reportReason.getDescription());
        jdbcInsert.execute(reportData);
    }

    @Override
    public Page<ReportedNews> getReportedNews() {
        return null;
    }
}
































