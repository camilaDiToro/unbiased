package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import java.util.List;

public class ReportedNews {

    private final News news;
    private final User news_owner;
    private final List<ReportDetail> reports;

    public ReportedNews(News news, User news_owner, List<ReportDetail> reports) {
        this.news = news;
        this.news_owner = news_owner;
        this.reports = reports;
    }

    public News getNews() {
        return news;
    }

    public User getNews_owner() {
        return news_owner;
    }

    public List<ReportDetail> getReports() {
        return reports;
    }
}
