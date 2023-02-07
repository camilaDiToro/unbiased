package ar.edu.itba.paw.model.exeptions;

public class InvalidFilterException extends RuntimeException{

    private static final String STRING_MSG = "The filter %s is invalid";

    public InvalidFilterException(String filter) {
        super(String.format(STRING_MSG, filter));
    }

    public InvalidFilterException(String filter, Throwable var1) {
        super(String.format(STRING_MSG, filter), var1);
    }
}
