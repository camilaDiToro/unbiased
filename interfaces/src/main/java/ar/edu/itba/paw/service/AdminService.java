package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;

public interface AdminService {
    void reportNews(News news, ReportReason reportReason);
    Page<ReportedNews> getReportedNews(int page, NewsOrder ns);
    Page<ReportDetail> getReportedNewsDetail(int page, News news);
    void deleteNews(News news);
    void makeUserAdmin(User user);
    boolean hasReported(News news);
}
