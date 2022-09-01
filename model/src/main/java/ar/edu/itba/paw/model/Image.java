package ar.edu.itba.paw.model;

public class Image {

    private final long imageId;
    private final byte[] bytes;
    private final String dataType;

    public Image(long imageId, byte[] bytes, String dataType) {
        this.imageId = imageId;
        this.bytes = bytes;
        this.dataType = dataType;
    }

    public long getImageId() {
        return imageId;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getDataType() {
        return dataType;
    }
}
