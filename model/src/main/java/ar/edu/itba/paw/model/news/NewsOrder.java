package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.exeptions.InvalidOrderException;

public enum NewsOrder {

    NEW("NEW", " creation_date desc", "order.new"),

    TOP("TOP", " accesses desc", "order.top");

    private final String description;
    private final String query;

    private final static String pagedQuery = "LIMIT :pageSize OFFSET :offset";

    private final String interCode;

    public String getText() {
        return text;
    }

    private final String text = toString();

    NewsOrder(String description, String query, String interCode){
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

    public String getQueryPaged() {
        return query + " " + pagedQuery;
    }

    public String getInterCode() {
        return interCode;
    }

    public static NewsOrder getByValue(String value){
        try{
            return NewsOrder.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidOrderException(value,e);
        }
    }
}
