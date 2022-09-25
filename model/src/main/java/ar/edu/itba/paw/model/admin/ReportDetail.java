package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class ReportDetail {
    private final User reporter;
    private final LocalDateTime creationDate;
    private final ReportReason reason;

    public ReportDetail(User reporter, LocalDateTime creationDate, String reason) {
        this.reporter = reporter;
        this.creationDate = creationDate;
        this.reason = ReportReason.valueOf(reason);
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
}
