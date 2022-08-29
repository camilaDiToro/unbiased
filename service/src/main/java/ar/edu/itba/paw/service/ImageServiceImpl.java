package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService{

    private final ImageDao imageDao;

    @Autowired
    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Image loadImage(byte[] image) {
        return null;
    }
}
