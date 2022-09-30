package ar.edu.itba.paw.model.admin;

public enum ReportOrder {

    REP_COUNT_DESC("REP_COUNT_DESC", "report_count desc", "reportOrder.reportCountDesc"),
    REP_COUNT_ASC("REP_COUNT_ASC", "report_count asc", "reportOrder.reportCountAsc"),
    REP_DATE_DESC("REP_DATE_DESC", "MAX(report_date) asc", "reportOrder.reportDateDesc"),
    REP_DATE_ASC("REP_DATE_ASC", "MIN(report_date) desc", "reportOrder.reportDateAsc");

    private final String description;
    private final String query;
    private final String interCode;

    ReportOrder(String description, String query, String interCode){
        this.description = description;
        this.query = query;
        this.interCode = interCode;
    }

    public String getDescription() {
        return description;
    }

    public String getQuery() {
        return query;
    }

    public String getInterCode() {
        return interCode;
    }
}
