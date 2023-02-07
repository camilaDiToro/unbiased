package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.InvalidTimeConstraintException;


public enum TimeConstraint {
    HOUR("timeConstraint.hour", " (CURRENT_DATE - interval '1 hour') "),
    DAY("timeConstraint.day", " CURRENT_DATE "),
    WEEK("timeConstraint.week", " (CURRENT_DATE - interval '1 week') "),
    ALLTIME("timeConstraint.alltime", " (timestamp '-infinity') ");

    public String getInterCode() {
        return interCode;
    }

    public String getText() {
        return text;
    }

    private final String interCode;
    private final String text = toString();

    public String getMinimumDateQuery() {
        return minimumDateQuery;
    }

    private final String minimumDateQuery;

    TimeConstraint(String interCode, String minimumDateQuery) {
        this.interCode = interCode;
        this.minimumDateQuery = minimumDateQuery;
    }
    public static TimeConstraint getByValue(String value){
        try{
            return TimeConstraint.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidTimeConstraintException(value,e);
        }
    }
}
