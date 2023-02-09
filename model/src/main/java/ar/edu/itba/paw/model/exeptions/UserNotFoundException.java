package ar.edu.itba.paw.model.exeptions;

public class UserNotFoundException extends RuntimeException {

    private static final String ID_MSG = "User of id %d not found";

    public UserNotFoundException(long id) {
        super(String.format(ID_MSG, id));
    }

    public UserNotFoundException(long id, Throwable var1) {
        super(String.format(ID_MSG, id),var1);
    }

    public UserNotFoundException() {
    }

}
