package ar.edu.itba.paw.model.news;

public class UpvoteAction {
    private long newsId;
    private boolean active;


    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
