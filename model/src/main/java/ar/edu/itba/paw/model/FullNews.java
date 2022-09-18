package ar.edu.itba.paw.model;

public class FullNews {
    private News news;
    private int readTime;
    private int upvotes;
    private User creator;
    private Positivity positivity;

    public double getPositiveValue() {
        return positiveValue;
    }

    private final double positiveValue;



    public FullNews(News news, User creator, int upvotes,Positivity positivity, double positiveValue) {
        this.news = news;
        this.upvotes = upvotes;
        this.creator = creator;
        this.positivity = positivity;
        this.positiveValue = positiveValue;

        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(news.getBody()));
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
