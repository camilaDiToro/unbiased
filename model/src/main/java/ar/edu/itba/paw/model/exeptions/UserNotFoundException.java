package ar.edu.itba.paw.model.exeptions;

public class UserNotFoundException extends RuntimeException {

    public static final String ID_MSG = "User of id %d not found";
    public static final String EMAIL_MSG = "User with email %s not found";

    public UserNotFoundException(String s) {
        super(s);
    }

    public UserNotFoundException(String s, Throwable var1) {
        super(s,var1);
    }

    public UserNotFoundException() {
    }
}
