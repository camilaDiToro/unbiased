package ar.edu.itba.paw.model.exeptions;

public class ImageNotFoundException extends RuntimeException  {

    public static final String ID_MSG = "Image of id %d not found";

    public ImageNotFoundException(String s) {
        super(s);
    }

    public ImageNotFoundException(String s, Throwable var1) {
        super(s,var1);
    }

}
