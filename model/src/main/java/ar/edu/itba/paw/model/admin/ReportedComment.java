package ar.edu.itba.paw.model.admin;


import ar.edu.itba.paw.model.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Entity
@Table(name="comment_report",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "comment_id" }) } )

public class ReportedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reported_comments_seq")
    @SequenceGenerator(name="reported_comments_seq", sequenceName = "reported_comments_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User reporter;

    @Column(name="report_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    private ReportReason reason;

    public ReportedComment(Comment comment, User reporter, ReportReason reason) {
        this.comment = comment;
        this.creationDate = LocalDateTime.now();
        this.reporter = reporter;
        this.reason = reason;
    }

    public String getFormattedDate(Locale locale) {
        return creationDate
                .format(DateTimeFormatter.ofLocalizedDate( FormatStyle.FULL )
                        .withLocale( locale));
    }

    ReportedComment() {
        // hibernate
    }

    public long getId() {
        return id;
    }
}
