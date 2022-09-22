package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.user.User;

import java.time.LocalDateTime;

public class ReportDetail {

    private final User reporter;
    private final LocalDateTime creationDate;
    private final String reason;

    public ReportDetail(User reporter, LocalDateTime creationDate, String reason) {
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

    public String getReason() {
        return reason;
    }
}
