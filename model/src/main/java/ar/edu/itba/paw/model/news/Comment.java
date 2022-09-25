package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.user.User;

import java.time.LocalDateTime;

public class Comment {
    User user;
    String comment;
    LocalDateTime date;

    public Comment(User user, String comment, LocalDateTime date) {
        this.user = user;
        this.comment = comment;
        this.date = date;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
