package ar.edu.itba.paw.model.exeptions;

public class InvalidTimeConstraintException extends RuntimeException{

    public static final String STRING_MSG = "The time constraint %s is invalid";

    public InvalidTimeConstraintException(String s) {
        super(s);
    }

    public InvalidTimeConstraintException(String s, Throwable var1) {
        super(s,var1);
    }
}