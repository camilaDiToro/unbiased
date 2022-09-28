package ar.edu.itba.paw.model.news;

public class UpvoteActionResponse {

    public UpvoteActionResponse(long upvotes, boolean active) {
        this.upvotes = upvotes;
        this.active = active;
    }
    private long upvotes;
    private boolean active;


    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
