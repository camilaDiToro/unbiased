package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;

public interface ImageService {

    Image loadImage(byte[] image);
}
