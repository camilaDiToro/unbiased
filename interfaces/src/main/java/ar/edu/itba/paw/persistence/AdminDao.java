package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;

public interface AdminDao {
    void reportNews(long newsId, long userId, ReportReason reportReason);
    Page<ReportedNews> getReportedNews();
}
