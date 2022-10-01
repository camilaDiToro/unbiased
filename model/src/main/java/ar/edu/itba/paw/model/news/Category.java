package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;

public enum Category {
    FOR_ME("categories.forMe"),
    ALL("categories.all"),
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

    public static Category getByValue(String value){
        try{
            return Category.valueOf(value);
        }catch (IllegalArgumentException e){
            throw new InvalidCategoryException();
        }
    }

}