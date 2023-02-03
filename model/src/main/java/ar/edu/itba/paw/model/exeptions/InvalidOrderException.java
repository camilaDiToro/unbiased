package ar.edu.itba.paw.model.exeptions;

public class InvalidOrderException extends RuntimeException{

    public static final String STRING_MSG = "The order %s is invalid";
    public InvalidOrderException(String s) {
        super(s);
    }

    public InvalidOrderException(String s, Throwable var1) {
        super(s, var1);
    }
}
