package ar.edu.itba.paw.model.exeptions;

public class InvalidTimeConstraintException extends RuntimeException{

    private static final String STRING_MSG = "The time constraint %s is invalid";

    public InvalidTimeConstraintException(String s) {
        super(String.format(STRING_MSG, s));
    }

    public InvalidTimeConstraintException(String s, Throwable var1) {
        super(String.format(STRING_MSG, s),var1);
    }
}