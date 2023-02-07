package ar.edu.itba.paw.model.exeptions;

import ar.edu.itba.paw.model.news.Category;

public class InvalidCategoryException extends RuntimeException{

    private static final String STRING_MSG = "The category %s is invalid";

    public InvalidCategoryException(String category) {
        super(String.format(STRING_MSG, category));
    }

    public InvalidCategoryException(Category category) {
        super(String.format(STRING_MSG, category));
    }

    public InvalidCategoryException(String category, Throwable var1) {
        super(String.format(STRING_MSG, category),var1);
    }
}
