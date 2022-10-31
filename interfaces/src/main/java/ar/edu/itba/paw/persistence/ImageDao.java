package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageDao {

    Optional<Image> getImageById(long id);
    Image uploadImage(byte[] bytes, String dataType);
    void deleteImage(long id);
}
