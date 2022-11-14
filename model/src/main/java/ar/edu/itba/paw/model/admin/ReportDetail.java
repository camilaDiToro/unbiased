package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.converter.LocalDateTimeConverter;
import ar.edu.itba.paw.model.news.News;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "report")
public class ReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_seq")
    @SequenceGenerator(name="report_id_seq", sequenceName = "report_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public ReportDetail(final News news, final User reporter, final LocalDateTime creationDate, final ReportReason reason) {
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

    public String getFormattedDate(final Locale locale) {
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

    public void setReporter(final User reporter) {
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

    public void setReason(final ReportReason reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ReportDetail that = (ReportDetail) o;
        return id == that.id && reporter.equals(that.reporter) && news.equals(that.news) && reason == that.reason;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reporter, news, reason);
    }
}
