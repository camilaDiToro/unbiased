package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_image_id_seq")
    @SequenceGenerator(name="image_image_id_seq", sequenceName = "image_image_id_seq", allocationSize = 1)
    @Column(name = "image_id")
    private final long imageId;

    @Column(name = "bytes")
    private final byte[] bytes;

    @Column(name = "data_type")
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
