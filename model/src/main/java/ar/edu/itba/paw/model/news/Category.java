package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Category {
    FOR_ME("categories.forMe", false),
    ALL("categories.all", false),
    TOURISM("categories.tourism"),
    SHOW("categories.entertainment"),
    POLITICS("categories.politics"),
    ECONOMICS("categories.economics"),
    SPORTS("categories.sports"),
    TECHNOLOGY("categories.technology");

    private final String interCode;
    private final boolean trueCategory;

    Category(String interCode, boolean trueCategory) {
        this.interCode = interCode;
        this.trueCategory = trueCategory;
    }

    Category(String interCode) {
        this(interCode, true);
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

    public static Category getByCode(String description){
        for (Category c : Category.values()) {
            if (c.getInterCode().equals(description) ) {
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

    public boolean isTrueCategory() {
        return trueCategory;
    }

    public static Iterable<Category> getTrueCategories() {
        return Arrays.stream(values()).filter(c -> c.isTrueCategory()).collect(Collectors.toList());
    }
}