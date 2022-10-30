package ar.edu.itba.paw.persistence.old;


import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class ImageJpaDao implements ImageDao {

    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageJpaDao.class);

    @Override
    public Optional<Image> getImageById(long id) {
        final TypedQuery<Image> query = entityManager.createQuery("FROM Image AS img WHERE img.imageId = :id",Image.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public long uploadImage(byte[] bytes, String dataType) {
        Image image = new Image(bytes, dataType);
//        final Map<String, Object> imageData = new HashMap<>();
//        imageData.put("bytes", bytes);
//        imageData.put("data_type", dataType);
//        long id = jdbcInsert.executeAndReturnKey(imageData).longValue();
        long id = image.getImageId();
        entityManager.persist(image);
        LOGGER.debug("Uploaded image with id {}", id);
        return id;
    }

    @Override
    public void deleteImage(long id) {
        LOGGER.debug("Deleting image with id {}", id);
        final TypedQuery<Image> query = entityManager.createQuery("DELETE FROM Image AS img WHERE img.imageId = :id",Image.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
