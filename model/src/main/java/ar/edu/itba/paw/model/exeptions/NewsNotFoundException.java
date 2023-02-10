package ar.edu.itba.paw.model.exeptions;

public class NewsNotFoundException extends RuntimeException {

    private static final String ID_MSG = "Article of id %d not found";

    public NewsNotFoundException(long id) {
        super(String.format(ID_MSG, id));
    }

    public NewsNotFoundException(long id, Throwable var1) {
        super(String.format(ID_MSG, id),var1);
    }
}
