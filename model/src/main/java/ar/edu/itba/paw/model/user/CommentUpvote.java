package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comment_upvotes")
public class CommentUpvote {

    CommentUpvote() {

    }

    public CommentUpvote(Comment coment, long userId) {
        this.comment = coment;
        this.userId = userId;
        this.date = Timestamp.valueOf(LocalDateTime.now());

    }

    public CommentUpvote(Comment comment, long userId, boolean value) {
        this(comment , userId);
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CommentUpvote))
            return false;
        CommentUpvote aux = (CommentUpvote) obj;
        return aux.comment.equals(comment) && aux.userId == userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_upvote_seq")
    @SequenceGenerator(name="comment_upvote_seq", sequenceName = "comment_upvote_seq", allocationSize = 1)
    private Long id;

    @Column(name= "upvote", nullable = false)
    private boolean value;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    @Column(name= "user_id", nullable = false)
    private long userId;

    @Column(name= "interaction_date", nullable = false)
    private Timestamp date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


}
