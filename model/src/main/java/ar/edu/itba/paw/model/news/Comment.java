package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.user.CommentUpvote;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {

    @OneToMany(mappedBy="comment",fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @MapKey(name = "userId")
    private Map<Long, CommentUpvote> upvoteMap;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @SequenceGenerator(name="comment_seq", sequenceName = "comment_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @Column(name = "comment")
    private String comment;

    @Column(name = "commented_date")
    private Timestamp commentedDate;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.MERGE)
    private List<ReportedComment> reports;

    @Transient
    private LocalDateTime date;

    private boolean deleted;

    public News getNews() {
        return news;
    }

    public void setNews(final News news) {
        this.news = news;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="news_id", nullable=false)
    private News news;

    Comment() {

    }

    public Comment(final User user, String comment, final News news) {
        this.user = user;
        this.comment = comment;
        this.date = LocalDateTime.now();
        this.news = news;
        this.commentedDate = Timestamp.valueOf(date);
    }

    public Comment(final User user, String comment,final  News news,final  LocalDateTime date) {
        this(user, comment, news);
        this.date = date;
        this.commentedDate = Timestamp.valueOf(date);
    }

    public List<ReportedComment> getReports() {
        return reports;
    }

    public void setReports(final List<ReportedComment> reports) {
        this.reports = reports;
    }

    public PositivityStats getPositivityStats() {
        final Collection<CommentUpvote> set = upvoteMap.values();
        final int total = set.size();
        final int upvotes =
                set.stream().map(u -> u.isValue() ? 1 : 0)
                        .reduce(0, Integer::sum);
        final int downvotes = total - upvotes;
        return new PositivityStats(upvotes, downvotes);
    }

    @Override
    public boolean equals(final Object obj) {
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

    public void setUser(final User user) {
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

    public void setDate(final LocalDateTime date) {
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

    public Map<Long, CommentUpvote> getUpvoteMap() {
        return upvoteMap;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(final Timestamp commentedDate) {
        this.commentedDate = commentedDate;
    }
}
