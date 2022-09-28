package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.LoggedUserParameters;
import ar.edu.itba.paw.model.user.User;

public class FullNews {
    private News news;
    private int readTime;
    private User creator;
    private PositivityStats positivityStats;

    private LoggedUserParameters loggedUserParameters;




    public FullNews(News news, User creator, PositivityStats positivityStats, LoggedUserParameters loggedUserParameters) {
        this.news = news;

        this.creator = creator;
        this.positivityStats = positivityStats;
        this.loggedUserParameters = loggedUserParameters;

        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(news.getBody()));
    }

    public FullNews(News news, User creator,  PositivityStats positivityStats) {
        this(news, creator, positivityStats, null);
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




    public PositivityStats getPositivityStats() {
        return positivityStats;
    }
}
