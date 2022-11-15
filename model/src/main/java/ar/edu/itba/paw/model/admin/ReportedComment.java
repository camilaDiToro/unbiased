package ar.edu.itba.paw.model.admin;


import ar.edu.itba.paw.model.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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

    public void setComment(final Comment comment) {
        this.comment = comment;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(final User reporter) {
        this.reporter = reporter;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setReason(final ReportReason reason) {
        this.reason = reason;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false, unique = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User reporter;

    @Column(name="report_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "reason")
    private ReportReason reason;

    public ReportedComment(final Comment comment, final User reporter, final ReportReason reason) {
        this.comment = comment;
        this.creationDate = LocalDateTime.now();
        this.reporter = reporter;
        this.reason = reason;
    }

    public String getFormattedDate(final Locale locale) {
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
