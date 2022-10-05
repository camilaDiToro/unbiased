package ar.edu.itba.paw.model.exeptions;

public class UserNotAuthorized extends RuntimeException{

    public UserNotAuthorized() {
    }

    public UserNotAuthorized(Throwable var1) {
        super(var1);
    }
}
