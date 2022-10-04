package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Category{

    TOURISM("categories.tourism", 2),
    SHOW("categories.entertainment", 3),
    POLITICS("categories.politics", 4),
    ECONOMICS("categories.economics", 5),
    SPORTS("categories.sports", 6),
    TECHNOLOGY("categories.technology", 7),
    FOR_ME("categories.forMe", false, 0),
    ALL("categories.all", false, 1);

    private final String interCode;
    private final boolean trueCategory;

    private final int order;

    private static final Comparator<Category> comparator = Comparator.comparingInt(Category::getOrder);

    Category(String interCode, boolean trueCategory, int order) {
        this.interCode = interCode;
        this.trueCategory = trueCategory;
        this.order  = order;
    }

    Category(String interCode, int order) {
        this(interCode, true, order);
    }

    public String getInterCode() {
        return interCode;
    }

    public int getOrder() {
        return order;
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
        return Arrays.stream(values()).filter(Category::isTrueCategory).collect(Collectors.toList());
    }

    public static List<Category> categoriesInOrder() {

        return Arrays.stream(values()).sorted(comparator).collect(Collectors.toList());
    }
}