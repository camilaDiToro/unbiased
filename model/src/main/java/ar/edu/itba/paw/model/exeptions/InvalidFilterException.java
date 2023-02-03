package ar.edu.itba.paw.model.exeptions;

public class InvalidFilterException extends RuntimeException{

    public static final String STRING_MSG = "The filter %s is invalid";

    public InvalidFilterException(String s) {
        super(s);
    }

    public InvalidFilterException(String s, Throwable var1) {
        super(s, var1);
    }
}
