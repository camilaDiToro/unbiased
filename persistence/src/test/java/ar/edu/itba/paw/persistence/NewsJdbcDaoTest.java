package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.NewsOrder;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    protected static final String NEWS_TABLE = "news";
    protected static final String CATEGORY_TABLE = "news_category";
    //USERS DATA
    protected static final String EMAIL = "juan@gmail.com";

    //NEWS DATA
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";
    protected static final int PAGE_SIZE = 1;
    protected static LocalDateTime CREATE_TIME = LocalDateTime.now();

    private User getMockUser() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        User user = userDao.createIfNotExists(usBuilder);
        return user;
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        newsDao = new NewsJdbcDao(ds, categoryDao);
        userDao = new UserJdbcDao(ds);
        categoryDao = new CategoryJdbcDao(ds);
    }

    @Test
    public void testCreateNews() {
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        //Optional<User> mayBeUser = userDao.getUserById(user.getId());
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        // 3. validaciones
        assertNotNull(user);
        assertNotNull(news);
        assertEquals(TITTLE, news.getTitle());
        assertEquals(BODY, news.getBody());
    }

    @Test
    public void testFindByNewsId() {
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        Optional<News> optionalNews = newsDao.getById(news.getNewsId());
        assertNotNull(optionalNews);
        if (optionalNews.isPresent())
            assertEquals(news.getNewsId(), optionalNews.get().getNewsId());
    }

    @Test
    public void testFindByNewsIdFailure() {
        Optional<News> news = newsDao.getById(80);
        assertFalse(news.isPresent());
    }

    @Test
    public void testFindByAuthorId() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<News> maybeNews = newsDao.getById(news.getNewsId());

        assertNotNull(news);
        assertEquals(maybeNews.get().getNewsId(), news.getNewsId());
    }

    @Test
    public void testGetNewsInCategoryDao(){
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        assertNotNull(news);

        List<News> newsList = newsDao.getNews(PAGE_SIZE, NewsOrder.NEW);

        // 3. validaciones
        assertEquals(PAGE_SIZE, newsList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetNewsByCategory(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        boolean addCategory = categoryDao.addCategoryToNews(news.getNewsId(), Category.SPORTS);
        List<News> newsList = newsDao.getNewsByCategory(PAGE_SIZE, Category.SPORTS, NewsOrder.NEW);

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
        // 1. precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        // 2. ejercitacion
        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        // 3. validaciones
        assertEquals(PAGE_SIZE, newsDao.getTotalPagesAllNews());
    }

    @Test
    public void testGetAllNewsFromUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        List<News> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, news.getCreatorId(), NewsOrder.NEW);
        assertEquals(1, newsList.size());
    }

        /*
    public List<News> getSavedNews(int page, User user, NewsOrder ns)
    public List<News> getAllNewsFromUser(int page, long userId, NewsOrder ns) {
    public List<News> getSavedNewsFromUser(int page, long userId, NewsOrder ns) {
    private List<News> getNewsWithRatingFromUser(int page, long userId, NewsOrder ns, boolean upvote) {
    public List<News> getNewsUpvotedByUser(int page, long userId, NewsOrder ns) {
    public List<News> getNewsDownvotedByUser(int page, long userId, NewsOrder ns) {
    public int getTotalPagesNewsFromUser(int page, long userId) {
    private int getTotalPagesNewsFromUserRating(int page, long userId, boolean upvoted) {
     public int getTotalPagesNewsFromUserUpvoted(int page, long userId) {
     public int getTotalPagesNewsFromUserDownvoted(int page, long userId) {
     public int getTotalPagesNewsFromUserSaved(int page, long userId) {
    public void deleteNews(long newsId) {
    public int getTotalPagesCategory(Category category) {
    public int getUpvotes(Long newsId) {
    public Rating upvoteState(News news, User user) {
    public void setRating(Long newsId, Long userId, Rating rating) {
    public double getPositivityValue(Long newsId) {
    public void saveNews(News news, User user) {
    public boolean isSaved(News news, User user) {
    public void removeSaved(News news, User user) {

*/
}
