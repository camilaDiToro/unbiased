package ar.edu.itba.paw.model.exeptions;

public class UserNotAuthorizedException extends RuntimeException{

    public UserNotAuthorizedException(String s) {
        super(s);
    }

    public UserNotAuthorizedException(String s, Throwable var1) {
        super(s);
    }
}
