package ar.edu.itba.paw.model.exeptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(Throwable var1) {
        super(var1);
    }
}
