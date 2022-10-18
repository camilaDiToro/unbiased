package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.news.News;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "upvotes")
public class Upvote {

    Upvote() {

    }

    public Upvote(News news, long userId) {
        this.news = news;
        this.userId = userId;
        this.date = Timestamp.valueOf(LocalDateTime.now());

    }

    public Upvote(News news, long userId, boolean value) {
        this(news , userId);
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(news, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Upvote))
            return false;
        Upvote aux = (Upvote) obj;
        return aux.news.equals(news) && aux.userId == userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "upvote_seq")
    @SequenceGenerator(name="upvote_seq", sequenceName = "upvote_seq", allocationSize = 1)
    private Long id;

    @Column(name= "upvote", nullable = false)
    private boolean value;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", referencedColumnName = "news_id")
    private News news;

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

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
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
