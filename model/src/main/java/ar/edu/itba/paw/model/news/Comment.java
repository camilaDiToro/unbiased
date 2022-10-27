package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "comments")
public class Comment {


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(Timestamp commentedDate) {
        this.commentedDate = commentedDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name="comment_seq", sequenceName = "comment_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    User user;
    @Column(name = "comment")
    String comment;

    @Column(name = "commented_date")
    Timestamp commentedDate;

    public List<ReportedComment> getReports() {
        return reports;
    }

    public void setReports(List<ReportedComment> reports) {
        this.reports = reports;
    }

    @OneToMany(mappedBy = "comment")
    List<ReportedComment> reports;

    @Transient
    LocalDateTime date;

    private boolean deleted;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @ManyToOne
    @JoinColumn(name="news_id", nullable=false)
    News news;

    Comment() {

    }

    public Comment(User user, String comment, News news) {
        this.user = user;
        this.comment = comment;
        this.date = LocalDateTime.now();
        this.news = news;
        this.commentedDate = Timestamp.valueOf(date);
    }

    public Comment(User user, String comment, News news, LocalDateTime date) {
        this(user, comment, news);
        this.date = date;
        this.commentedDate = Timestamp.valueOf(date);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Comment))
            return false;
        return id.equals(((Comment) obj).id);
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = true;
    }

    @PostLoad
    private void postLoad() {
        this.date = commentedDate.toLocalDateTime();
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

    public String getFormattedDate(Locale locale) {
        return date
                .format(DateTimeFormatter.ofLocalizedDate( FormatStyle.FULL )
                        .withLocale( locale));
    }

    public TimeUtils.Amount getAmountAgo() {
        return TimeUtils.calculateTimeAgoWithPeriodAndDuration(date);
    }
}
