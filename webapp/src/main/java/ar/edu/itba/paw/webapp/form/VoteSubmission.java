package ar.edu.itba.paw.webapp.form;

public class VoteSubmission {
    private boolean active;
    private int newsId;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
