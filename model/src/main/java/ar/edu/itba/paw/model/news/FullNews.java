package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.user.LoggedUserParameters;
import ar.edu.itba.paw.model.user.User;

public class FullNews {
    private News news;
    private int readTime;
    private int upvotes;
    private User creator;
    private Positivity positivity;

    private LoggedUserParameters loggedUserParameters;

    public double getPositiveValue() {
        return positiveValue;
    }

    private final double positiveValue;



    public FullNews(News news, User creator, int upvotes,Positivity positivity, double positiveValue, LoggedUserParameters loggedUserParameters) {
        this.news = news;
        this.upvotes = upvotes;
        this.creator = creator;
        this.positivity = positivity;
        this.positiveValue = positiveValue;
        this.loggedUserParameters = loggedUserParameters;

        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(news.getBody()));
    }

    public FullNews(News news, User creator, int upvotes,Positivity positivity, double positiveValue) {
        this(news, creator, upvotes, positivity, positiveValue, null);
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

    public int getUpvotes() {
        return upvotes;
    }

    public User getUser() {
        return creator;
    }


    public Positivity getPositivity() {
        return positivity;
    }
}
