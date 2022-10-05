package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.exeptions.InvalidOrderException;

public enum NewsOrder {

    TOP("TOP", "accesses desc", "order.top"),
    NEW("NEW", "creation_date desc", "order.new");

    private final String description;
    private final String query;

    private final String interCode;

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

    public String getInterCode() {
        return interCode;
    }

    public static NewsOrder getByValue(String value){
        try{
            return NewsOrder.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidOrderException();
        }
    }
}
