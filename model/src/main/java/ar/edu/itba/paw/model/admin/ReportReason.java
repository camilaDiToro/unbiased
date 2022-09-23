package ar.edu.itba.paw.model.admin;

public enum ReportReason {
    INAPP("INAP", "report.inappropiate"),
    NOT_SERIOUS("NOT_SERIOUS", "report.notSerious"),
    VIOLENT("VIOLENT", "report.violent"),
    LIE("LIE", "report.lie");

    private final String description;
    private final String interCode;

    ReportReason(String description, String interCode){
        this.description = description;
        this.interCode = interCode;
    }

    public String getDescription() {
        return description;
    }

    public String getInterCode() {
        return interCode;
    }
}
