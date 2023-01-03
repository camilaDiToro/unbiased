package ar.edu.itba.paw.model.exeptions;

public class NewsNotFoundException extends RuntimeException {

    public NewsNotFoundException() {
    }

    public NewsNotFoundException(Throwable var1) {
        super(var1);
    }
}
