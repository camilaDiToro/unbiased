package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService{

    private final ImageDao imageDao;

    @Autowired
    public ImageServiceImpl(final ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public Optional<Image> getImageById(long id) {
        return imageDao.getImageById(id);
    }

    @Override
    @Transactional
    public long uploadImage(final byte[] bytes, String dataType) {
        return imageDao.uploadImage(bytes, dataType).getImageId();
    }

    @Override
    @Transactional
    public void deleteImage(long id) {
        imageDao.deleteImage(id);
    }
}
