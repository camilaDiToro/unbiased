package ar.edu.itba.paw.model;

public enum Category {

    TOURISM("categories.tourism"),
    SHOW("categories.entertainment"),
    POLITICS("categories.politics"),
    ECONOMICS("categories.economics"),
    SPORTS("categories.sports"),
    TECHNOLOGY("categories.technology");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return getDescription();
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