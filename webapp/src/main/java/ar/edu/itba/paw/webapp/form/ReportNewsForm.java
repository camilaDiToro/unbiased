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

    @NotNull
    @NotBlank
    @Length(max=400)
    @NotExistingReportReason
    private String reason = "INAP";

    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
