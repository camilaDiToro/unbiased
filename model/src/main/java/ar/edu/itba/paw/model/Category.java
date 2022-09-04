package ar.edu.itba.paw.model;

public enum Category {

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

    public static Category getByDescription(String description){
        for (Category c : Category.values()) {
            if (c.getDescription().equals(description) ) {
                return c;
            }
        }
        return null;
    }

}