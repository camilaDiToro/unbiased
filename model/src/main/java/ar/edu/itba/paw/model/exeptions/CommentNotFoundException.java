package ar.edu.itba.paw.model.exeptions;


public class CommentNotFoundException extends RuntimeException  {

    private static final String ID_MSG = "Comment of id %d not found";

    public CommentNotFoundException(long id) {
        super(String.format(ID_MSG, id));
    }

    public CommentNotFoundException(long id, Throwable var1) {
        super(String.format(ID_MSG, id), var1);
    }

}