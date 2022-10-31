package ar.edu.itba.paw.persistence.old;


import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Image> ROW_MAPPER = (rs, rowNum) -> new Image(rs.getLong("image_id"), rs.getBytes("bytes"),rs.getString("data_type") );

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageJdbcDao.class);

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("image").usingGeneratedKeyColumns("image_id");
    }

    @Override
    public Optional<Image> getImageById(long id) {
        return jdbcTemplate.query("SELECT * FROM image WHERE image_id = ?",
                new Object[] { id }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public long uploadImage(byte[] bytes, String dataType) {
        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("bytes", bytes);
        imageData.put("data_type", dataType);
        long id = jdbcInsert.executeAndReturnKey(imageData).longValue();
        LOGGER.debug("Uploaded image with id {}", id);
        return id;
    }

    @Override
    public void deleteImage(long id) {
        LOGGER.debug("Deleting image with id {}", id);
        jdbcTemplate.update("DELETE FROM image WHERE image_id = ?",id);
    }
}
