package ar.edu.itba.paw.model.exeptions;

public class InvalidCategoryException extends RuntimeException{

    public static final String STRING_MSG = "The category %s is invalid";

    public InvalidCategoryException(String s) {
        super(s);
    }

    public InvalidCategoryException(String s, Throwable var1) {
        super(s,var1);
    }
}
