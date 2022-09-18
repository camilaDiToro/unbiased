package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface ImageDao {

    Optional<Image> getImageById(long id);
    long uploadImage(byte[] bytes, String dataType);
    void deleteImage(long id);
}
