package ar.edu.itba.paw.model.user;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "saved_news")
public class Saved {

    Saved() {

    }

    public Saved(long newsId, long userId) {
        this.newsId = newsId;
        this.userId = userId;
        this.date = Timestamp.valueOf(LocalDateTime.now());

    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Saved))
            return false;
        Saved aux = (Saved) obj;
        return aux.newsId == newsId && aux.userId == userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "upvote_seq")
    @SequenceGenerator(name="upvote_seq", sequenceName = "upvote_seq", allocationSize = 1)
    private Long id;

    @Column(name= "news_id", nullable = false)
    private long newsId;

    @Column(name= "user_id", nullable = false)
    private long userId;

    @Column(name= "saved_date", nullable = false)
    private Timestamp date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
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
