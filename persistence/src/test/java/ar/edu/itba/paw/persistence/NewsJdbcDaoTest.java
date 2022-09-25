package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class NewsJdbcDaoTest {
    private NewsJdbcDao newsDao;
    private UserJdbcDao userDao;
    @Autowired
    private DataSource ds;
    private CategoryDao categoryDao;
    protected JdbcTemplate jdbcTemplate;

    //TABLES
    protected static final String NEWS_TABLE = "news";
    protected static final String CATEGORY_TABLE = "news_category";
    protected static final String SAVED_TABLE = "saved_news";
    //USERS DATA
    protected static final String EMAIL = "user@gmail.com";

    //NEWS DATA
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";
    protected static final int PAGE_SIZE = 1;

    private User getMockUser() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        User user = userDao.create(usBuilder);
        return user;
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        newsDao = new NewsJdbcDao(ds, categoryDao);
        userDao = new UserJdbcDao(ds);
        categoryDao = new CategoryJdbcDao(ds);
        //adminDao = new AdminJdbcDaoTest(ds);
    }

    @Test
    public void testCreateNews() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        Optional<User> mayBeUser = userDao.getUserById(user.getId());
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        assertTrue(mayBeUser.isPresent());
        assertNotNull(news);
        assertEquals(TITTLE, news.getTitle());
        assertEquals(BODY, news.getBody());
    }

    //loggedUser = null
    @Test
    public void testFindByNewsId() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();

        //loggedUser = null
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalNews = newsDao.getById(news.getNewsId(), null);
        if (optionalNews.isPresent())
            assertEquals(news.getNewsId(), optionalNews.get().getNews().getNewsId());

    }

    //loggedUser != null
    /*@Test
    public void testFindByNewsId() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();

        //loggedUser != null
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        PositivityStats positivityStats = new PositivityStats(0, 0);
        LoggedUserParameters loggedUserParameters = new LoggedUserParameters(Rating.UPVOTE, false);
        FullNews fullnews = new FullNews(news, user, null);
        News news1 = fullnews.getNews();
        Optional<FullNews> optionalNews = newsDao.getById(news1.getNewsId(), null);
        //if (optionalNews.isPresent())
        //    assertEquals(news.getNewsId(), optionalNews.get().getNews().getNewsId());
    }*/

    @Test
    public void testFindByNewsIdFailure() {
        Optional<FullNews> news = newsDao.getById(80, null);
        assertFalse(news.isPresent());
    }

    @Test
    public void testFindByAuthorId() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> maybeNews = newsDao.getById(news.getNewsId(), null);

        assertTrue(maybeNews.isPresent());
        assertEquals(maybeNews.get().getNews().getNewsId(), news.getNewsId());
    }

    @Test
    public void testDeleteNews() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.deleteNews(news.getCreatorId());

        List<FullNews> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, news.getCreatorId(), NewsOrder.NEW, null);
        assertEquals(0, newsList.size()); // TODO
    }

    @Test
    public void testGetNewsByCategory(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        boolean addCategory = categoryDao.addCategoryToNews(news.getNewsId(), Category.SPORTS);
        List<FullNews> newsList = newsDao.getNewsByCategory(PAGE_SIZE, Category.SPORTS, NewsOrder.NEW, null);

        assertEquals(1, newsList.size());
    }

    @Test
    public void testGetNewsCategory(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        boolean addCategory = categoryDao.addCategoryToNews(news.getNewsId(), Category.SPORTS);

        if(addCategory) {
            List<Category> catList = newsDao.getNewsCategory(news);
            assertEquals(1, catList.size());
        }
    }

    @Test
    public void testGetTotalPagesCategory(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, CATEGORY_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        boolean addCategory = categoryDao.addCategoryToNews(news.getNewsId(), Category.SPORTS);

        if(addCategory) {
            int pagesCategory = newsDao.getTotalPagesCategory(Category.SPORTS);
            assertEquals(PAGE_SIZE, pagesCategory);
        }
    }

    @Test
    public void testGetTotalPageNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        assertEquals(PAGE_SIZE, newsDao.getTotalPagesAllNews());
    }

    @Test
    public void testGetAllNewsFromUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        List<FullNews> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, news.getCreatorId(), NewsOrder.NEW, null);
        assertEquals(1, newsList.size());

    }

    @Test
    public void testGetTotalPagesNewsFromUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        int newsList = newsDao.getTotalPagesNewsFromUser(PAGE_SIZE, news.getCreatorId());
        assertEquals(PAGE_SIZE, newsList);
    }

    @Test
    public void testSaveNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        newsDao.saveNews(news, user);
        FullNews fullNews = newsDao.getById(user.getId(), user.getId()).get();
        assertTrue(fullNews.getLoggedUserParameters().isSaved());
    }

    @Test
    public void testRemoveSavedNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        newsDao.saveNews(news, user);
        newsDao.removeSaved(news, user);
        assertTrue(newsDao.getSavedNews(PAGE_SIZE, user.getId(), NewsOrder.NEW, null).isEmpty()); //TODO

    }

    @Test
    public void testGetSavedNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.saveNews(news, user);

        List<FullNews> savedList = newsDao.getSavedNews(PAGE_SIZE, user.getId(), NewsOrder.NEW, null); //TODO
        assertEquals(1, savedList.size());
    }

    @Test
    public void testGetTotalPagesNewsFromUserSaved(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.saveNews(news, user);

        int savedPages = newsDao.getTotalPagesNewsFromUserSaved(PAGE_SIZE, news.getCreatorId());
        assertEquals(PAGE_SIZE, savedPages);
    }

    @Test
    public void testSetRating(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.DOWNVOTE);
        //assertTrue(newsDao.getUpvotes(news.getNewsId()).  );
    }

    @Test
    public void testUpvoteState(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        FullNews fullNews = newsDao.getById(news.getNewsId(), user.getId()).get();

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.UPVOTE);
        assertEquals(Rating.UPVOTE, fullNews.getLoggedUserParameters().getPersonalRating());
    }

    @Test
    public void testGetUpvotes(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        FullNews fullNews = newsDao.getById(news.getNewsId(), user.getId()).get();

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.UPVOTE);
        int rating = fullNews.getPositivityStats().getNetUpvotes();
        assertEquals(1, rating);
    }

    @Test
    public void testGetNewsUpvotedByUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        FullNews fullNews = newsDao.getById(news.getNewsId(), user.getId()).get();

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.UPVOTE);
        List<FullNews> rating = newsDao.getNewsUpvotedByUser(PAGE_SIZE, news.getCreatorId(), NewsOrder.NEW, null);
        assertEquals(1, rating.size());
    }

    @Test
    public void testGetNewsDownvotedByUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        FullNews fullNews = newsDao.getById(news.getNewsId(), user.getId()).get();

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.DOWNVOTE);
        List<FullNews> rating = newsDao.getNewsDownvotedByUser(PAGE_SIZE, news.getCreatorId(), NewsOrder.NEW, null);
        assertEquals(1, rating.size());
    }

    @Test
    public void testGetTotalPagesNewsFromUserUpvoted(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.UPVOTE);
        int rating = newsDao.getTotalPagesNewsFromUserUpvoted(PAGE_SIZE, news.getCreatorId());
        assertEquals(1, rating);
    }

    @Test
    public void testGetTotalPagesNewsFromUserDownvoted(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        newsDao.setRating(news.getNewsId(), news.getCreatorId(), Rating.DOWNVOTE);
        int rating = newsDao.getTotalPagesNewsFromUserDownvoted(PAGE_SIZE, news.getCreatorId());
        assertEquals(1, rating);
    }
}
