package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.LoggedUserParameters;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.functional.GetNewsProfileFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class NewsJpaDao implements NewsDao {


    private static final double PAGE_SIZE = 10.0;
    private static final double COMMENT_PAGE_SIZE = 5.0;
    private static final double PROFILE_PAGE_SIZE = 5.0;
    private static final double RECOMMENDATION_PAGE_SIZE = 10.0;

    final Logger LOGGER = LoggerFactory.getLogger(NewsJpaDao.class);


    @Override
    public News create(News.NewsBuilder newsBuilder) {


        return null;
    }

    @Override
    public int getTotalPagesAllNews() {
        return 0;
    }

    @Override
    public List<FullNews> getNews(int page, String query, NewsOrder ns, Long loggedUser) {
        return null;
    }

    @Override
    public int getTotalPagesAllNews(String query) {
        return 0;
    }

    @Override
    public Optional<FullNews> getById(long id, Long loggedUser) {
        return null;

    }

    @Override
    public Optional<News> getSimpleNewsById(long id) {
        return null;
    }

    @Override
    public List<FullNews> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser) {
        return null;
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        return null;
    }

    @Override
    public void deleteNews(News news) {
    }

    @Override
    public int getTotalPagesCategory(Category category) {
        return 0;
    }

    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {

    }

    @Override
    public void saveNews(News news, User user) {

    }

    private int getTotalPagesComments(long newsId) {
        return 0;
    }

    @Override
    public void addComment(User user, News news, String comment) {

    }

    @Override
    public Page<Comment> getComments(long newsId, int page) {
        return null;
    }

    @Override
    public Page<FullNews> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory) {
        return null;
    }

    @Override
    public void removeSaved(News news, User user) {
    }


    @Override
    public List<FullNews> getRecommendation(int page, User user, NewsOrder newsOrder) {
       return null;
    }

    @Override
    public int getTodayNewsPageCount() {
        return 0;
    }




    Page<FullNews> getAllNewsFromUser(int page, User user, NewsOrder ns, Long loggedUser) {

       return null;

    }

    private Page<FullNews> getNewsWithRatingFromUser(int page, User user, NewsOrder ns, Long loggedUser, boolean upvote) {

       return null;
    }

    Page<FullNews> getNewsUpvotedByUser(int page, User user, NewsOrder ns, Long loggedUser) {
        return null;
    }

    Page<FullNews> getNewsDownvotedByUser(int page, User user, NewsOrder ns, Long loggedUser) {
        return null;
    }

    int getTotalPagesNewsFromUser(int page, User user) {
        return 0;
    }

    int getTotalPagesNewsFromUserRating(int page, long userId, boolean upvoted) {
        return 0;
    }

    int getTotalPagesNewsFromUserSaved(int page, User user) {
        return 0;
    }
}
