package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.NotExistingReportReason;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ReportNewsForm {

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @NotNull(message = "reportnewsform.reason.notnull")
    @NotBlank(message = "reportnewsform.reason.not.blank")
    @Length(max=400, message = "reportnewsform.reason.length")
    @NotExistingReportReason(message = "reportnewsform.reason.notfound")
    private String reason = "INAP";

    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
