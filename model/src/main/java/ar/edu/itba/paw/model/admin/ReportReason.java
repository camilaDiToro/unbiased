package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.news.TimeConstraint;

public enum ReportReason {
    INAP("INAP", "report.inappropiate"),
    NOT_SERIOUS("NOT_SERIOUS", "report.notSerious"),
    VIOLENT("VIOLENT", "report.violent"),
    LIE("LIE", "report.lie");

    private final String description;
    private final String interCode;

    ReportReason(final String description, final String interCode) {
        this.description = description;
        this.interCode = interCode;
    }

    public String getDescription() {
        return description;
    }

    public String getInterCode() {
        return interCode;
    }

    public static ReportReason getByValue(String value){
        for (ReportReason reason : values()) {
            if (reason.getInterCode().equals(value))
                return reason;
        }
        throw new InvalidCategoryException();
    }
}
