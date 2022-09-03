package ar.edu.itba.paw.model;

public enum NewsOrder {

    TOP("TOP", "accesses desc"),
    NEW("NEW", "creation_date desc");

    private final String description;
    private final String query;

    NewsOrder(String description, String query){
        this.description = description;
        this.query = query;
    }

    public String getDescription() {
        return description;
    }

    public String getQuery() {
        return query;
    }
}
