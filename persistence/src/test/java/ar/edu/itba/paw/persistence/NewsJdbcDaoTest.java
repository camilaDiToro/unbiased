package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.User;
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
    private SimpleJdbcInsert jdbcInsert;


    //TABLES
    protected static final String NEWS_TABLE = "news";
    protected static final String CATEGORY_TABLE = "news_category";
    protected static final String SAVED_TABLE = "saved_news";
    protected static final String COMMENT_TABLE = "comments";

    //USERS DATA
    protected static final String EMAIL = "user@gmail.com";

    //NEWS DATA
    protected static final String TITTLE = "titulo";
    protected static final String SUBTITTLE = "subtitulo";
    protected static final String BODY = "cuerpo";
    protected static final String COMMENT = "comment";
    protected static final int PAGE_SIZE = 1;

    private User getMockUser() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        return userDao.create(usBuilder);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        newsDao = new NewsJdbcDao(ds, categoryDao);
        userDao = new UserJdbcDao(ds);
        categoryDao = new CategoryJdbcDao(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName(NEWS_TABLE).usingGeneratedKeyColumns("newsId");

    }

    @Test
    public void testCreateNews() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        Optional<User> optionalUser = userDao.getUserById(user.getId());
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        optionalUser.ifPresent(opt -> assertNotNull(news));
        assertEquals(TITTLE, news.getTitle());
        assertEquals(BODY, news.getBody());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByNewsIdNotLoggedUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();

        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalNews = newsDao.getById(news.getNewsId(), null);

        optionalNews.ifPresent(opt -> assertEquals(news.getNewsId(), optionalNews.get().getNews().getNewsId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByNewsIdLoggedUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();

        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        FullNews fullnews = new FullNews(news, user, null);
        News news1 = fullnews.getNews();
        Optional<FullNews> optionalNews = newsDao.getById(news1.getNewsId(), null);

        optionalNews.ifPresent(opt -> assertEquals(news.getNewsId(), optionalNews.get().getNews().getNewsId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByNewsIdFailure() {
        Optional<FullNews> optionalFullNews = newsDao.getById(80, null);

        assertFalse(optionalFullNews.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByAuthorId() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);

        optionalFullNews.ifPresent(opt -> assertEquals(optionalFullNews.get().getNews().getNewsId(), news.getNewsId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testDeleteNews() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.deleteNews(news);
        List<FullNews> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, user, NewsOrder.NEW, null);

        assertEquals(0, newsList.size());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetSimpleNewsById() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional <News> optionalFullNews = newsDao.getSimpleNewsById(news.getNewsId());

        optionalFullNews.ifPresent(opt -> assertEquals(news.getNewsId(), optionalFullNews.get().getNewsId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetNewsByCategory(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional <News> optionalFullNews = newsDao.getSimpleNewsById(news.getNewsId());
        boolean addCategory = categoryDao.addCategoryToNews(optionalFullNews.get().getNewsId(), Category.SPORTS);
        List<FullNews> newsList = newsDao.getNewsByCategory(PAGE_SIZE, Category.SPORTS, NewsOrder.NEW, null);

        if(addCategory)
            assertEquals(1, newsList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
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
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
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
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetTotalPageNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);

        assertEquals(PAGE_SIZE, newsDao.getTotalPagesAllNews());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetAllNewsFromUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        List<FullNews> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, user, NewsOrder.NEW, null);

        assertEquals(1, newsList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetTotalPagesNewsFromUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        int newsList = newsDao.getTotalPagesNewsFromUser(PAGE_SIZE, user);

        assertEquals(PAGE_SIZE, newsList);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testSaveNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);
        newsDao.saveNews(optionalFullNews.get().getNews(), optionalFullNews.get().getUser());

        optionalFullNews.ifPresent(opt -> assertTrue(optionalFullNews.get().getLoggedUserParameters().isSaved()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
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

        assertTrue(newsDao.getSavedNews(PAGE_SIZE, user.getId(), NewsOrder.NEW, null).isEmpty());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetSavedNews(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);

        newsDao.saveNews(optionalFullNews.get().getNews(), optionalFullNews.get().getUser());
        List<FullNews> savedList = newsDao.getSavedNews(PAGE_SIZE, optionalFullNews.get().getUser().getId(), NewsOrder.NEW, null);

        assertEquals(1, savedList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SAVED_TABLE));
    }

    @Test
    public void testGetTotalPagesNewsFromUserSaved(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SAVED_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.saveNews(news, user);
        int savedPages = newsDao.getTotalPagesNewsFromUserSaved(PAGE_SIZE, user);

        assertEquals(PAGE_SIZE, savedPages);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SAVED_TABLE));
    }

    @Test
    public void testGetNewsUpvotedByUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);

        newsDao.setRating(optionalFullNews.get().getNews().getNewsId(), optionalFullNews.get().getNews().getCreatorId(), Rating.UPVOTE);
        List<FullNews> rating = newsDao.getNewsUpvotedByUser(PAGE_SIZE, user, NewsOrder.NEW, null);

        optionalFullNews.ifPresent(opt->assertEquals(1, rating.size()));
        //assertEquals(Rating.UPVOTE, optionalFullNews.get().getLoggedUserParameters().getPersonalRating());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }


    @Test
    public void testGetNewsDownvotedByUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);

        newsDao.setRating(optionalFullNews.get().getNews().getNewsId(), optionalFullNews.get().getNews().getCreatorId(), Rating.DOWNVOTE);
        List<FullNews> rating = newsDao.getNewsDownvotedByUser(PAGE_SIZE, user, NewsOrder.NEW, null);

        optionalFullNews.ifPresent(opt->assertEquals(1, rating.size()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetTotalPagesNewsFromUserUpvoted(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.setRating(news.getNewsId(), user.getId(), Rating.UPVOTE);
        int rating = newsDao.getTotalPagesNewsFromUserUpvoted(PAGE_SIZE, user);

        assertEquals(PAGE_SIZE, rating);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetTotalPagesNewsFromUserDownvoted(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.setRating(news.getNewsId(), user.getId(), Rating.DOWNVOTE);
        int rating = newsDao.getTotalPagesNewsFromUserDownvoted(PAGE_SIZE, user);

        assertEquals(PAGE_SIZE, rating);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }


    @Test
    public void testGetComments(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, COMMENT_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        newsDao.addComment(user, news, COMMENT);
        Page<Comment> commentPage = newsDao.getComments(news.getNewsId(), PAGE_SIZE);

        assertEquals(commentPage.getContent().get(0).getComment(), COMMENT);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
    }

    @Test
    public void testGetRecommendation(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NEWS_TABLE);

        User user = getMockUser();
        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITTLE, SUBTITTLE);
        News news = newsDao.create(nwBuilder);
        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);

        List<FullNews> recommendation = newsDao.getRecommendation(PAGE_SIZE, optionalFullNews.get().getUser(), NewsOrder.NEW);

        assertEquals(PAGE_SIZE, recommendation.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }
}
