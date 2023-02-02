package ar.edu.itba.paw.model.exeptions;

public class CommentNotFoundException extends RuntimeException  {

    public static final String ID_MSG = "Comment of id %d not found";

    public CommentNotFoundException(String s) {
        super(s);
    }

    public CommentNotFoundException(String s, Throwable var1) {
        super(var1);
    }

}