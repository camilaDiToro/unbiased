package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.Entity;


public class ReportedNews {
    private final News news;
    private final User newsOwner;
    private final int reportCount;

    public ReportedNews(final News news, final User newsOwner, final int reportCount) {
        this.news = news;
        this.newsOwner = newsOwner;
        this.reportCount = reportCount;
    }

    public News getNews() {
        return news;
    }

    public User getNewsOwner() {
        return newsOwner;
    }

    public int getReportCount() {
        return reportCount;
    }
}
