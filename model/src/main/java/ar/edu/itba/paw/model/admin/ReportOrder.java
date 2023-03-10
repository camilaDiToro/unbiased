package ar.edu.itba.paw.model.admin;

import ar.edu.itba.paw.model.exeptions.InvalidOrderException;

public enum ReportOrder {

    REP_COUNT_DESC("REP_COUNT_DESC", "COUNT(n.user_id) desc", "reportOrder.reportCountDesc"),
    REP_COUNT_ASC("REP_COUNT_ASC", "COUNT(n.user_id) asc", "reportOrder.reportCountAsc"),
    REP_DATE_DESC("REP_DATE_DESC", "MAX(report_date) desc", "reportOrder.reportDateDesc"),
    REP_DATE_ASC("REP_DATE_ASC", "MIN(report_date) asc", "reportOrder.reportDateAsc");

    private final String description;
    private final String query;
    private final String interCode;

    ReportOrder(final String description, final String query,final String interCode){
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

    public static ReportOrder getByValue(final String value){
        try{
            return ReportOrder.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidOrderException(value,e);
        }
    }
}
