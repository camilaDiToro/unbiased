package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Entity
@Table(name = "report")
public class ReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_seq")
    @SequenceGenerator(name="report_id_seq", sequenceName = "report_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User reporter;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", referencedColumnName = "news_id", nullable = false)
    private News news;

    @Column(name="report_date", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReportReason reason;


    /* package */ ReportDetail(){
        // Just for hibernate
    }

    public ReportDetail(News news, User reporter, LocalDateTime creationDate, ReportReason reason) {
        this.news = news;
        this.reporter = reporter;
        this.creationDate = creationDate;
        this.reason = reason;
    }

    public User getReporter() {
        return reporter;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public ReportReason getReason() {
        return reason;
    }

    public String getFormattedDate(Locale locale) {
        return creationDate
                .format(DateTimeFormatter.ofLocalizedDate( FormatStyle.FULL )
                        .withLocale( locale));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }
}
