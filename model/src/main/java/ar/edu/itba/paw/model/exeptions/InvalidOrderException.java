package ar.edu.itba.paw.model.exeptions;

public class InvalidOrderException extends RuntimeException{

    private static final String STRING_MSG = "The order %s is invalid";

    public InvalidOrderException(String order) {
        super(String.format(STRING_MSG, order));
    }

    public InvalidOrderException(String order, Throwable var1) {
        super(String.format(STRING_MSG, order), var1);
    }
}
