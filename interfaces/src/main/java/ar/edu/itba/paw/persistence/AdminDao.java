package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.NewsOrder;

public interface AdminDao {
    void reportNews(long newsId, Long loggedUser, ReportReason reportReason);
    Page<ReportedNews> getReportedNews(int page, NewsOrder ns);
    Page<ReportDetail> getReportedNewsDetail(int page, long newsId);
    void makeUserAdmin(long userId);

    boolean hasReported(long newsId, Long loggedUser);
}
