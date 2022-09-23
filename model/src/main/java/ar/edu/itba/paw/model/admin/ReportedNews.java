package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import java.util.List;

public class ReportedNews {

    private final News news;
    private final User newsOwner;
    private final int reportCount;

    public ReportedNews(News news, User newsOwner, int reportCount) {
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
