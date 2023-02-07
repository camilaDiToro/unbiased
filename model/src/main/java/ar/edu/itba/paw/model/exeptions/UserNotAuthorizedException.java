package ar.edu.itba.paw.model.exeptions;

public class UserNotAuthorizedException extends RuntimeException{

    public UserNotAuthorizedException() {
    }

    public UserNotAuthorizedException(Throwable var1) {
        super(var1);
    }
}
