package ar.edu.itba.paw.model;

public class FullNews {
    private News news;
    private int readTime;
    private User creator;
    private NewsStats newsStats;

    private LoggedUserParameters loggedUserParameters;




    public FullNews(News news, User creator,  NewsStats newsStats, LoggedUserParameters loggedUserParameters) {
        this.news = news;

        this.creator = creator;
        this.newsStats = newsStats;
        this.loggedUserParameters = loggedUserParameters;

        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(news.getBody()));
    }

    public FullNews(News news, User creator,  NewsStats newsStats ) {
        this(news, creator, newsStats, null);
    }

    public boolean hasLoggedUserParameters() {
        return loggedUserParameters != null;
    }

    public LoggedUserParameters getLoggedUserParameters() {
//        if (!hasLoggedUserParameters())
//            throw new NoSuchElementException();

        return loggedUserParameters;
    }

    public News getNews() {
        return news;
    }

    public int getReadTime() {
        return readTime;
    }



    public User getUser() {
        return creator;
    }




    public NewsStats getNewsStats() {
        return newsStats;
    }
}
