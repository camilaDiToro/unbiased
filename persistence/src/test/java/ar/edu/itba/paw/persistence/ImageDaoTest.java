package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageDaoTest {

    private ImageJdbcDao imageDao;
    private UserJdbcDao userDao;
    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    protected static final String IMAGE_TABLE = "image";
    private static final File IMAGE = new File("src/test/resources/imageTest.png");

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        imageDao = new ImageJdbcDao(ds);
        userDao = new UserJdbcDao(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds).withTableName(IMAGE_TABLE);
    }

    private User getMockUser() {
        User.UserBuilder usBuilder = new User.UserBuilder("user@gmail.com");
        return userDao.create(usBuilder);
    }

    @Test
    public void testGetImageById() throws IOException {
        final byte[] image = Files.readAllBytes(IMAGE.toPath());
        User user = getMockUser();
        final RowMapper<Image> imageRowMapper = (rs, rowNum) -> new Image(
                        rs.getLong("image_id"),
                        rs.getBytes("bytes"),
                        rs.getString("data_type"));

        //simpleJdbcInsert.execute((Map<String, ?>) imageRowMapper);

        final Map<String, Object> imageInfo = new HashMap<>();
        imageInfo.put("userid", 1);
        imageInfo.put("image", image);

        simpleJdbcInsert.execute(imageInfo);

        final Optional<Image> resultImage = imageDao.getImageById(1);
        assertNotNull(resultImage);
    }
}
