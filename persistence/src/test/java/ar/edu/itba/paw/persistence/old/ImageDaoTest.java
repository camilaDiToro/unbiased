package ar.edu.itba.paw.persistence.old;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.persistence.ImageJdbcDao;
import ar.edu.itba.paw.persistence.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
//import java.util.Optional;
//
//@Rollback
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//@Transactional
//public class ImageDaoTest {
//
//    private ImageJdbcDao imageDao;
//
//    @Autowired
//    private DataSource ds;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcImageInsert;
//
//    //TABLES
//    private static final String IMAGE_TABLE = "image";
//
//    //IMAGE_DATA
//    private static final String IMAGE_TYPE = "image/jpeg";
//    private static final int IMAGE_ID = 1;
//    private static final byte[] IMAGE_DATA = new byte[100];
//
//    private void createImage() {
//        Map<String, Object> imageData = new HashMap<>();
//        imageData.put("image_id", IMAGE_ID);
//        imageData.put("bytes", IMAGE_DATA);
//        imageData.put("data_type", IMAGE_TYPE);
//        jdbcImageInsert.execute(imageData);
//    }
//
//    @Before
//    public void setUp() {
//        imageDao = new ImageJdbcDao(ds);
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcImageInsert = new SimpleJdbcInsert(ds).withTableName(IMAGE_TABLE);
//    }
//
//
//    @Test
//    public void testGetImageById(){
//        createImage();
//        Optional<Image> optionalImage = imageDao.getImageById(IMAGE_ID);
//
//        optionalImage.ifPresent(opt -> assertEquals(optionalImage.get().getImageId(), IMAGE_ID));
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
//    }
//
//    @Test
//    public void testFailGetImageById() {
//        Optional<Image> optionalImage = imageDao.getImageById(IMAGE_ID);
//
//        assertFalse(optionalImage.isPresent());
//        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
//    }
//    @Test
//    public void testUploadImage() {
//        imageDao.uploadImage(IMAGE_DATA, IMAGE_TYPE);
//
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
//    }
//
//    @Test
//    public void testDeleteImage() {
//        createImage();
//        imageDao.deleteImage(IMAGE_ID);
//
//        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
//    }
//}
