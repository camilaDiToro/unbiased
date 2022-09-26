package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {
    Optional<Image> getImageById(long id);
    Long uploadImage(byte[] bytes, String dataType);
    void deleteImage(long id);
}
