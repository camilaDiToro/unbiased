package ar.edu.itba.paw.model.exeptions;

public class NewsNotFoundException extends RuntimeException {

    public static final String ID_MSG = "Article of id %d not found";

    public NewsNotFoundException(String s) {
        super(s);
    }

    public NewsNotFoundException(String s, Throwable var1) {
        super(s,var1);
    }
}
