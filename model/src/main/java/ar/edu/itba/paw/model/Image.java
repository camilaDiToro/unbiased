package ar.edu.itba.paw.model;

public class Image {

    private final long id;
    private final byte[] bytes;

    public Image(long id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }
}
