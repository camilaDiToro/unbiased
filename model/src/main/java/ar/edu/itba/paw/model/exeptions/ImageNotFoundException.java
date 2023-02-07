package ar.edu.itba.paw.model.exeptions;

public class ImageNotFoundException extends RuntimeException  {

    public static final String ID_MSG = "Image of id %d not found";

    public ImageNotFoundException(long id) {
        super(String.format(ID_MSG, id));
    }

    public ImageNotFoundException(long id, Throwable var1) {
        super(String.format(ID_MSG, id),var1);
    }

}
