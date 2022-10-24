package ar.edu.itba.paw.model.admin;


import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="comment_report")
public class ReportedComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reported_comments_seq")
    @SequenceGenerator(name="reported_comments_seq", sequenceName = "reported_comments_seq", allocationSize = 1)

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

    public ReportReason getReportReason() {
        return reportReason;
    }

    public void setReportReason(ReportReason reportReason) {
        this.reportReason = reportReason;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User reporter;

    @Column(name="report_date")
    private LocalDateTime creationDate;

    private ReportReason reportReason;

    public ReportedComment(Comment comment, User reporter, ReportReason reportReason) {
        this.comment = comment;
        this.creationDate = LocalDateTime.now();
        this.reporter = reporter;
        this.reportReason = reportReason;
    }
}
