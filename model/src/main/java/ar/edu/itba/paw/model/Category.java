package ar.edu.itba.paw.model;

public enum Category {
    TOP("TOP"),
    NEW("NUEVO"),
    TOURISM("TURISMO"),
    SHOW("ESPECTÁCULO"),
    POLITICS("POLÍTICA"),
    ECONOMICS("ECONOMÍA"),
    SPORTS("DEPORTES"),
    TECHNOLOGY("TECNOLOGÍA");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return ordinal();
    }

    public static Category getById(long id) {
        for (Category c : Category.values()) {
            if (c.ordinal() == id) {
                return c;
            }
        }
        return null;
    }

}