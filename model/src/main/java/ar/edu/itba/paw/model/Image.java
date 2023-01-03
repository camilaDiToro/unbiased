package ar.edu.itba.paw.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_image_id_seq")
    @SequenceGenerator(name="image_image_id_seq", sequenceName = "image_image_id_seq", allocationSize = 1)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "bytes")
    private byte[] bytes;

    @Column(name = "data_type")
    private String dataType;

    /* package */ Image(){
        // Just for hibernate
    }

    public Image(Long imageId,final byte[] bytes, String dataType) {
        this.imageId = imageId;
        this.bytes = bytes;
        this.dataType = dataType;
    }

    public Image(final byte[] bytes, String dataType) {
        this.bytes = bytes;
        this.dataType = dataType;
    }

    public Long getImageId() {
        return imageId;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getDataType() {
        return dataType;
    }
}
