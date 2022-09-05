package ar.edu.itba.paw.model;

public enum Category {

    TOURISM("categories.tourism"),
    SHOW("categories.entertainment"),
    POLITICS("categories.politics"),
    ECONOMICS("categories.economics"),
    SPORTS("categories.sports"),
    TECHNOLOGY("categories.technology");

    private final String interCode;

    Category(String interCode) {
        this.interCode = interCode;
    }

    public String getInterCode() {
        return interCode;
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

    public static Category getByInterCode(String description){
        for (Category c : Category.values()) {
            if (c.getInterCode().equals(description) ) {
                return c;
            }
        }
        return null;
    }

}