package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;

import java.util.Optional;

public interface ImageService {
    Optional<Image> getImageById(long id);
    long uploadImage(byte[] bytes, String dataType);
}
