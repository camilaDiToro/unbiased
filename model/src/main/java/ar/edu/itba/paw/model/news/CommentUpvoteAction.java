package ar.edu.itba.paw.model.news;

public class CommentUpvoteAction {
    private long commentId;
    private boolean active;


    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
