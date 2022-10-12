package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.persistence.functional.GetNewsProfileFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class NewsJpaDao implements NewsDao {

    @PersistenceContext
    private EntityManager entityManager;


    private static final int PAGE_SIZE = 10;
    private static final int COMMENT_PAGE_SIZE = 5;
    private static final int PROFILE_PAGE_SIZE = 5;
    private static final int RECOMMENDATION_PAGE_SIZE = 10;

    final Logger LOGGER = LoggerFactory.getLogger(NewsJpaDao.class);

    private final Map<ProfileCategory, GetNewsProfileFunction> profileFunctions;


    public NewsJpaDao() {
        profileFunctions = new HashMap<>();
        profileFunctions.put(ProfileCategory.DOWNVOTED, this::getNewsDownvotedByUser);
        profileFunctions.put(ProfileCategory.UPVOTED, this::getNewsUpvotedByUser);
        profileFunctions.put(ProfileCategory.SAVED, this::getSavedNews);
        profileFunctions.put(ProfileCategory.MY_POSTS, this::getAllNewsFromUser);
    }


    @Override
    public News create(News.NewsBuilder newsBuilder) {
        final News news = newsBuilder.build();
        entityManager.persist(news);

        return news;
    }

    @Override
    public int getTotalPagesAllNews() {
        long elemCount = entityManager.createQuery("SELECT COUNT(f) from News f",Long.class).getSingleResult();
        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public List<News> getNews(int page, String query, NewsOrder ns, Long loggedUser) {
        Query queryObj = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE title LIKE :query ORDER BY " + ns.getQueryPaged())
                .setParameter("query", "%" + query + "%");

        List<News> news = getNewsOfPage(queryObj, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return news;
    }

    @Override
    public int getTotalPagesAllNews(String query) {
        long elemCount = entityManager.createQuery("SELECT count(f) from News f WHERE f.title LIKE :query",Long.class)
                .setParameter("query", '%' + query + '%').getSingleResult();

        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public Optional<News> getById(long id, Long loggedUser) {
        final TypedQuery<News> typedQuery = entityManager.createQuery("SELECT f from News f WHERE f.newsId = :newsId ",News.class).setParameter("newsId", id);
        Optional<News> news = typedQuery.getResultList().stream().findFirst();
        if (loggedUser != null && news.isPresent())
            news.get().setUserSpecificVariables(loggedUser);

        return news;

    }


    @Override
    public List<News> getNewsByCategory(int page, Category category, NewsOrder ns, Long loggedUser) {
        Query query = entityManager.createNativeQuery("SELECT news_id FROM news f NATURAL JOIN news_category WHERE category_id = :category ORDER BY " +ns.getQueryPaged())
                .setParameter("category", category.getId());


        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return news;
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        return null;
    } // TODO: delete

    @Override
    public void deleteNews(News news) {
        entityManager.createQuery("DELETE FROM News n WHERE n.newsId = :id").setParameter("id", news.getNewsId());
    }

    @Override
    public int getTotalPagesCategory(Category category) {
        long elemCount = entityManager.createQuery("SELECT count(f) from News f WHERE :category MEMBER OF f.categories",Long.class)
                .setParameter("category", category).getSingleResult();

        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public void setRating(News news, User user, Rating rating) {
        Map<Long, Upvote> upvoteMap = news.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
//            entityManager.createQuery("DELETE FROM Upvote u WHERE u.news.newsId = :newsId AND u.userId = :userId")
//                    .setParameter("newsId", news)
//                    .setParameter("userId", user).executeUpdate();
            upvoteMap.remove(user.getId());
            return;
        }

//        Optional<Upvote> maybeUpvote = entityManager.createQuery("SELECT u from Upvote u WHERE u.news.newsId = :newsId AND u.userId = :userId", Upvote.class)
//            .setParameter("newsId", news)
//            .setParameter("userId", user).getResultList().stream().findFirst();
//        Upvote upvote = maybeUpvote.orElseGet(() -> new Upvote(getById(news, null).get(), user));
//        upvote.setValue(rating.equals(Rating.UPVOTE));
//        entityManager.persist(upvote);
        upvoteMap.put(user.getId(), new Upvote(news, user.getId(), rating.equals(Rating.UPVOTE)));
    }

    @Override
    public void saveNews(News news, User user) {
        Optional<Saved> maybeSaved = entityManager.createQuery("SELECT u from Saved u WHERE u.news = :news AND u.userId = :userId", Saved.class)
                .setParameter("news", news)
                .setParameter("userId", user.getId()).getResultList().stream().findFirst();

        if (!maybeSaved.isPresent()) {
            Saved saved = new Saved(news, user.getId());
            entityManager.persist(saved);
        }

    }

    private int getTotalPagesComments(long newsId) {
        long count = entityManager.createQuery("SELECT COUNT(c) from Comment c WHERE c.news.newsId = :newsId", Long.class)
                .setParameter("newsId", newsId).getSingleResult();
        return Page.getPageCount(count, COMMENT_PAGE_SIZE);
    }

    @Override
    public void addComment(User user, News news, String comment) {
        Comment commentObj = new Comment(user, comment, news);
        entityManager.persist(commentObj);
    }

    private List<Comment> getCommentsOfPage(Query query,int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) query.setParameter("pageSize", pageSize)
                .setParameter("offset", pageSize*(page-1))
                .getResultList().stream().map(o -> ((Integer)o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty())
            return new ArrayList<>();

        final TypedQuery<Comment> typedQuery = entityManager.createQuery("SELECT f from Comment f WHERE f.id IN :ids",Comment.class)
                .setParameter("ids", ids);

        List<Comment> comments = typedQuery.getResultList();
        return comments;
    }

    private List<News> getNewsOfPage(Query query,int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = getIdsOfPage(query, page, pageSize);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        final TypedQuery<News> typedQuery = entityManager.createQuery("SELECT f from News f WHERE f.id IN :ids",News.class)
                .setParameter("ids", ids);

        List<News> news = typedQuery.getResultList();
        return news;
    }

    private List<Long> getIdsOfPage(Query query,int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) query.setParameter("pageSize", pageSize)
                .setParameter("offset", pageSize*(page-1))
                .getResultList().stream().map(o -> ((Integer)o).longValue()).collect(Collectors.toList());

        return ids;
    }

    @Override
    public Page<Comment> getComments(long newsId, int page) {
        Query query = entityManager.createNativeQuery("SELECT f.id from comments f WHERE news_id = :newsId LIMIT :pageSize OFFSET :offset")
                .setParameter("newsId", newsId);
        List<Comment> comments = getCommentsOfPage(query, page, COMMENT_PAGE_SIZE);

        return new Page<>(comments, page, getTotalPagesComments(newsId));
    }

    @Override
    public Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory) {
        return profileFunctions.get(profileCategory).getNews(page, user, ns, loggedUser);
    }

    @Override
    public void removeSaved(News news, User user) {
        entityManager.createQuery("DELETE FROM Saved s WHERE s.news.newsId = :newsId AND s.userId = :userId")
                .setParameter("newsId", news.getNewsId())
                .setParameter("userId", user.getId());

    }

    @Override
    public List<News> getRecommendation(int page, User user, NewsOrder newsOrder) {
       return getNews(page, "",newsOrder, user.getId()); // TODO implement
    }

    @Override
    public int getTodayNewsPageCount() {
        return 1; // TODO implement
    }


    Page<News> getAllNewsFromUser(int page, User user, NewsOrder ns, Long loggedUser) {
        Query query  = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE creator = :userId order by " + ns.getQueryPaged())
                .setParameter("userId", user.getId());
        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);

        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return new Page<>(news, page, getTotalPagesNewsFromUser(user));
    }

    Page<News> getSavedNews(int page, User user, NewsOrder ns, Long loggedUser) {
        Query query = entityManager.createNativeQuery("SELECT news_id FROM saved_news NATURAL JOIN news f WHERE user_id = :userId order by " + ns.getQueryPaged())
                .setParameter("userId", user.getId());
        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return new Page<>(news, page, getTotalPagesNewsFromUser(user));
    }



    private Page<News> getNewsWithRatingFromUser(int page, User user, NewsOrder ns, Long loggedUser, boolean upvote) {
        Map<NewsOrder, String> map = new HashMap<>();
        map.put(NewsOrder.NEW, "u.news.date desc");
        map.put(NewsOrder.TOP, "u.news.accesses desc"); // TODO es feo pero por ahora no se me ocurre otra cosa
        Query query = entityManager.createNativeQuery("SELECT news_id FROM upvotes NATURAL JOIN news f WHERE upvote = :value AND user_id = :userId order by " + ns.getQueryPaged())
                .setParameter("value", upvote).setParameter("userId", user.getId());
        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return new Page<>(news, page, getTotalPagesNewsFromUserRating(user.getId(), upvote));
    }

    Page<News> getNewsUpvotedByUser(int page, User user, NewsOrder ns, Long loggedUser) {
        return getNewsWithRatingFromUser(page, user, ns, loggedUser, true);
    }

    Page<News> getNewsDownvotedByUser(int page, User user, NewsOrder ns, Long loggedUser) {
        return getNewsWithRatingFromUser(page, user, ns, loggedUser, false);

    }

    int getTotalPagesNewsFromUser(User user) {
        long elemCount =  entityManager.createQuery("SELECT count(f) from News f WHERE f.creator = :user",Long.class)
                .setParameter("user", user) .getSingleResult();
        return Page.getPageCount(elemCount, PROFILE_PAGE_SIZE);
    }

    int getTotalPagesNewsFromUserRating(long userId, boolean upvoted) {
        long elemCount =  entityManager.createQuery("SELECT count(u) from Upvote u WHERE u.userId = :user AND u.value = :value",Long.class)
                .setParameter("user", userId)
                .setParameter("value", upvoted).getSingleResult();
        return Page.getPageCount(elemCount, PROFILE_PAGE_SIZE);
    }

    int getTotalPagesNewsFromUserSaved(User user) {
        long elemCount =  entityManager.createQuery("SELECT u.upvoteSet.size from User u WHERE u.userId = :user",Long.class)
                .setParameter("user", user.getId())
                .getSingleResult();
        return Page.getPageCount(elemCount, PROFILE_PAGE_SIZE);
    }
}
