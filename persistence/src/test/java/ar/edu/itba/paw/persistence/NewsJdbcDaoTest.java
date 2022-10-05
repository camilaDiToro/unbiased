package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class NewsJdbcDaoTest {
    private NewsJdbcDao newsDao;
    @Autowired
    private DataSource ds;
    @Mock
    private CategoryDao categoryDao;
    protected JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcUserInsert;
    private SimpleJdbcInsert jdbcCategoryInsert;
    private SimpleJdbcInsert jdbcNewsInsert;

    private SimpleJdbcInsert jdbcSavedNewsInsert;

    private SimpleJdbcInsert jdbcNewsUpvotesInsert;

    private SimpleJdbcInsert jdbcNewsCommentsInsert;






    //TABLES
    protected static final String NEWS_TABLE = "news";
    protected static final String USER_TABLE = "users";

    protected static final String CATEGORY_TABLE = "news_category";
    protected static final String SAVED_TABLE = "saved_news";
    protected static final String COMMENT_TABLE = "comments";

    protected static final String UPVOTES_TABLE = "upvotes";


    //USERS DATA
    protected static final String EMAIL = "user@gmail.com";// por que protected???

    private static final long CREATOR_ID = 1;

    private static final UserStatus CREATOR_STATUS = UserStatus.REGISTERED;
    private static final long NEWS_ID = 1;

    private static final Timestamp NEWS_DATE = Timestamp.valueOf(LocalDateTime.now());

    private static final long NEWS_ACCESSES = 0;

    protected static final String COMMENT_CONTENT = "comment";


    //NEWS DATA
    protected static final String TITLE = "titulo";
    protected static final String SUBTITLE = "subtitulo";
    protected static final String BODY = "cuerpo";
    protected static final String COMMENT = "comment";
    protected static final int PAGE_SIZE = 1;

    private static final Category CATEGORY = Category.SPORTS;

    private static final User CREATOR = new User.UserBuilder(EMAIL).userId(CREATOR_ID).build();


    private static final News NEWS = new News.NewsBuilder(CREATOR_ID,BODY,TITLE,SUBTITLE).creationDate(NEWS_DATE.toLocalDateTime()).newsId(NEWS_ID).build();


    private void addCreatorToTable() {
        Map<String, Object> userValues = new HashMap<>();
        userValues.put("email", EMAIL);
        userValues.put("status", CREATOR_STATUS.getStatus());
        userValues.put("user_id", CREATOR_ID);
        jdbcUserInsert.execute(userValues);
    }

    private void addCategoryToTheNews() {
        Map<String, Object> categoryValues = new HashMap<>();
        categoryValues.put("category_id", CATEGORY.getId());
        categoryValues.put("news_id", NEWS_ID);
        jdbcCategoryInsert.execute(categoryValues);
    }

    private void addTheNewsToTable() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("body", BODY);
        newsValues.put("title", TITLE);
        newsValues.put("subtitle", SUBTITLE);
        newsValues.put("creator", CREATOR_ID);
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("creation_date", NEWS_DATE);
        newsValues.put("accesses", NEWS_ACCESSES);
        jdbcNewsInsert.execute(newsValues);
    }

    private void addTheNewsToSavedTableForCreator() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("user_id", CREATOR_ID);
        newsValues.put("saved_date", Timestamp.valueOf(LocalDateTime.now()));
        jdbcSavedNewsInsert.execute(newsValues);
    }

    private void addUpvoteFromCreatorToTheNews() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("user_id", CREATOR_ID);
        newsValues.put("upvote", true);
        newsValues.put("interaction_date", Timestamp.valueOf(LocalDateTime.now()));
        jdbcNewsUpvotesInsert.execute(newsValues);
    }

    private void addDownvoteFromCreatorToTheNews() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("user_id", CREATOR_ID);
        newsValues.put("upvote", false);
        newsValues.put("interaction_date", Timestamp.valueOf(LocalDateTime.now()));
        jdbcNewsUpvotesInsert.execute(newsValues);
    }

    private void addCommentFromCreatorToTheNews() {
        Map<String, Object> newsValues = new HashMap<>();
        newsValues.put("news_id", NEWS_ID);
        newsValues.put("user_id", CREATOR_ID);
        newsValues.put("comment", COMMENT_CONTENT);
        newsValues.put("commented_date", Timestamp.valueOf(LocalDateTime.now()));
        jdbcNewsCommentsInsert.execute(newsValues);
    }

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        newsDao = new NewsJdbcDao(ds, categoryDao);
        categoryDao = new CategoryJdbcDao(ds);
        jdbcUserInsert = new SimpleJdbcInsert(ds).withTableName(USER_TABLE);
        jdbcCategoryInsert = new SimpleJdbcInsert(ds).withTableName(CATEGORY_TABLE);
        jdbcNewsInsert = new SimpleJdbcInsert(ds).withTableName(NEWS_TABLE);
        jdbcSavedNewsInsert = new SimpleJdbcInsert(ds).withTableName(SAVED_TABLE);
        jdbcNewsUpvotesInsert = new SimpleJdbcInsert(ds).withTableName(UPVOTES_TABLE);
        jdbcNewsCommentsInsert = new SimpleJdbcInsert(ds).withTableName(COMMENT_TABLE);


    }

    @Test
    public void testCreateNews() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional<News> optionalNews = newsDao.getSimpleNewsById(NEWS_ID);


        assertEquals(TITLE, optionalNews.get().getTitle());
        assertEquals(CREATOR_ID, optionalNews.get().getCreatorId());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));

    }

    @Test
    public void testFindByNewsIdNotLoggedUser() {
        addCreatorToTable();
        addTheNewsToTable();

        Optional<FullNews> optionalNews = newsDao.getById(NEWS_ID, null);

        assertEquals(NEWS_ID, optionalNews.get().getNews().getNewsId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testFindByNewsIdFailure() {
        Optional<FullNews> optionalFullNews = newsDao.getById(80, null);

        assertFalse(optionalFullNews.isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testFindByAuthorId() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional<FullNews> optionalFullNews = newsDao.getById(NEWS_ID, null);

        assertEquals(optionalFullNews.get().getNews().getNewsId(), NEWS_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testDeleteNews() {
        addCreatorToTable();
        addTheNewsToTable();

        newsDao.deleteNews(NEWS);
        List<FullNews> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(0, newsList.size());
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
    }

    @Test
    public void testGetSimpleNewsById() {
        addCreatorToTable();
        addTheNewsToTable();
        Optional <News> optionalNews = newsDao.getSimpleNewsById(NEWS_ID);

        optionalNews.ifPresent(opt -> assertEquals(NEWS_ID, optionalNews.get().getNewsId()));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }



    @Test
    public void testGetNewsByCategory(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();
        List<FullNews> newsList = newsDao.getNewsByCategory(PAGE_SIZE, Category.SPORTS, NewsOrder.NEW, null);

        assertEquals(1, newsList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetNewsCategory(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();


        List<Category> catList = newsDao.getNewsCategory(NEWS);
        assertEquals(1, catList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetTotalPagesCategory(){
        addCreatorToTable();
        addTheNewsToTable();
        addCategoryToTheNews();
        int pagesCategory = newsDao.getTotalPagesCategory(Category.SPORTS);
        assertEquals(PAGE_SIZE, pagesCategory);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetTotalPageNews(){
        addCreatorToTable();
        addTheNewsToTable();

        assertEquals(PAGE_SIZE, newsDao.getTotalPagesAllNews());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetAllNewsFromUser() {
        addCreatorToTable();
        addTheNewsToTable();
        List<FullNews> newsList = newsDao.getAllNewsFromUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(1, newsList.size());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetTotalPagesNewsFromUser(){
        addCreatorToTable();
        addTheNewsToTable();
        int newsList = newsDao.getTotalPagesNewsFromUser(PAGE_SIZE, CREATOR);

        assertEquals(PAGE_SIZE, newsList);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetSavedNews(){
        addCreatorToTable();
        addTheNewsToTable();
        addTheNewsToSavedTableForCreator();

        List<FullNews> savedList = newsDao.getSavedNews(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(1, savedList.size());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SAVED_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SAVED_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testSaveNews(){
        addCreatorToTable();
        addTheNewsToTable();

        newsDao.saveNews(NEWS, CREATOR);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + NEWS_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SAVED_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SAVED_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testRemoveSavedNews(){
        addCreatorToTable();
        addTheNewsToTable();
        addTheNewsToSavedTableForCreator();

        newsDao.removeSaved(NEWS, CREATOR);
        List<FullNews> savedNews = newsDao.getSavedNews(PAGE_SIZE, CREATOR, NewsOrder.NEW, NEWS_ID).getContent();

        assertTrue(savedNews.isEmpty());


        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SAVED_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SAVED_TABLE, "news_id = " + NEWS_ID));
    }



    @Test
    public void testGetTotalPagesNewsFromUserSaved(){
        addCreatorToTable();
        addTheNewsToTable();
        addTheNewsToSavedTableForCreator();

        int savedPages = newsDao.getTotalPagesNewsFromUserSaved(PAGE_SIZE, CREATOR);

        assertEquals(PAGE_SIZE, savedPages);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SAVED_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SAVED_TABLE, "news_id = " + NEWS_ID));
    }

    @Test
    public void testGetNewsUpvotedByUser(){
        addCreatorToTable();
        addTheNewsToTable();
        addUpvoteFromCreatorToTheNews();

        List<FullNews> rating = newsDao.getNewsUpvotedByUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(1, rating.size());
    }


    @Test
    public void testGetNewsDownvotedByUser(){
        addCreatorToTable();
        addTheNewsToTable();
        addDownvoteFromCreatorToTheNews();

        List<FullNews> rating = newsDao.getNewsDownvotedByUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(1, rating.size());
    }

    @Test
    public void testDownvoteUpvotedByUser(){
        addCreatorToTable();
        addTheNewsToTable();
        addUpvoteFromCreatorToTheNews();

        newsDao.setRating(NEWS_ID, CREATOR_ID, Rating.DOWNVOTE);

        List<FullNews> ratingDownvoted = newsDao.getNewsDownvotedByUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();
        List<FullNews> ratingUpvoted = newsDao.getNewsUpvotedByUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(1, ratingDownvoted.size());
        assertEquals(0, ratingUpvoted.size());
    }

    @Test
    public void testRemoveUpvote(){
        addCreatorToTable();
        addTheNewsToTable();
        addUpvoteFromCreatorToTheNews();

        newsDao.setRating(NEWS_ID, CREATOR_ID, Rating.NO_RATING);

        List<FullNews> ratingUpvoted = newsDao.getNewsUpvotedByUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(0, ratingUpvoted.size());
    }

    @Test
    public void testRemoveDownvote(){
        addCreatorToTable();
        addTheNewsToTable();
        addDownvoteFromCreatorToTheNews();

        newsDao.setRating(NEWS_ID, CREATOR_ID, Rating.NO_RATING);

        List<FullNews> ratingDownvoted = newsDao.getNewsDownvotedByUser(PAGE_SIZE, CREATOR, NewsOrder.NEW, null).getContent();

        assertEquals(0, ratingDownvoted.size());
    }

    @Test
    public void testGetTotalPagesNewsFromUserUpvoted(){
        addCreatorToTable();
        addTheNewsToTable();
        addUpvoteFromCreatorToTheNews();


        int rating = newsDao.getTotalPagesNewsFromUserRating(PAGE_SIZE, CREATOR_ID, true);

        assertEquals(PAGE_SIZE, rating);

    }

    @Test
    public void testGetTotalPagesNewsFromUserDownvoted(){
        addCreatorToTable();
        addTheNewsToTable();
        addDownvoteFromCreatorToTheNews();


        int rating = newsDao.getTotalPagesNewsFromUserRating(PAGE_SIZE, CREATOR_ID, false);

        assertEquals(PAGE_SIZE, rating);
    }

    @Test
    public void testAddComments(){
        addCreatorToTable();
        addTheNewsToTable();

        newsDao.addComment(CREATOR, NEWS,COMMENT_CONTENT);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COMMENT_TABLE,
                "news_id = " + NEWS_ID +
                        " AND comment = " + COMMENT_CONTENT +
                        " AND user_id = " + CREATOR_ID));
    }


    @Test
    public void testGetComments(){
        addCreatorToTable();
        addTheNewsToTable();
        addCommentFromCreatorToTheNews();

        Page<Comment> commentPage = newsDao.getComments(NEWS_ID, PAGE_SIZE);

        assertEquals(commentPage.getContent().get(0).getComment(), COMMENT);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE,  "news_id = " + NEWS_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, COMMENT_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, COMMENT_TABLE,  "news_id = " + NEWS_ID));
    }

//    @Test
//    public void testGetRecommendation(){
//        addCreatorToTable();
//        News.NewsBuilder nwBuilder = new News.NewsBuilder(user.getId(), BODY, TITLE, SUBTITLE);
//        News news = newsDao.create(nwBuilder);
//        Optional<FullNews> optionalFullNews = newsDao.getById(news.getNewsId(), null);
//        List<FullNews> recommendation = newsDao.getRecommendation(PAGE_SIZE, optionalFullNews.get().getUser(), NewsOrder.NEW);
//
//        optionalFullNews.ifPresent(opt ->assertEquals(PAGE_SIZE, recommendation.size()));
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NEWS_TABLE));
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NEWS_TABLE, "news_id = " + news.getNewsId()));
//    }
}
