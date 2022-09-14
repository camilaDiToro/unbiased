package ar.edu.itba.paw.model;

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
}
