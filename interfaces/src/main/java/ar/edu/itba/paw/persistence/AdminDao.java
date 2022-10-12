package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

public interface AdminDao {
    void reportNews(News news, User reporter, ReportReason reportReason);
    Page<ReportedNews> getReportedNews(int page, ReportOrder reportOrder);
    Page<ReportDetail> getReportedNewsDetail(int page, News news);
    void makeUserAdmin(User user);
    boolean hasReported(News news, Long loggedUser);
}
