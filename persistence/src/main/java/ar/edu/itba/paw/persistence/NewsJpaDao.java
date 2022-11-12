package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.persistence.functional.GetNewsProfileFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NewsJpaDao implements NewsDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int SEARCH_PAGE_SIZE = 10;
    private static final int PAGE_SIZE = 10;
    private static final int PROFILE_PAGE_SIZE = 5;
    private static final int RECOMMENDATION_PAGE_SIZE = 10;

    final Logger LOGGER = LoggerFactory.getLogger(NewsJpaDao.class);

    private final Map<ProfileCategory, GetNewsProfileFunction> profileFunctions;

    public NewsJpaDao() {
        profileFunctions = new EnumMap<>(ProfileCategory.class);
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
    public List<News> getNewNews(int page, String query, Long loggedUser) {
        Query queryObj = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE LOWER(title) LIKE :query ORDER BY " + NewsOrder.NEW.getQueryPaged())
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(query.toLowerCase()) + "%");

        List<News> news = getNewsOfPage(queryObj, page, SEARCH_PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return news;
    }

    @Override
    public List<News> getTopNews(int page, String query, TimeConstraint timeConstraint, Long loggedUser) {
        Query queryObj = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE LOWER(title) LIKE :query AND creation_date >= "+timeConstraint.getMinimumDateQuery() + " ORDER BY " + NewsOrder.TOP.getQueryPaged())
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(query.toLowerCase()) + "%");

        List<News> news = getNewsOfPage(queryObj, page, SEARCH_PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return news;
    }


    @Override
    public int getTotalPagesAllNews(String query, TimeConstraint timeConstraint) {
        long elemCount = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE LOWER(title) LIKE :query AND creation_date >= " +timeConstraint.getMinimumDateQuery())
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(query.toLowerCase()) + "%")
                .getFirstResult();

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
    public List<News> getNewsByCategoryNew(int page, Category category, Long loggedUser) {
        Query query = entityManager.createNativeQuery("SELECT news_id FROM news f NATURAL JOIN news_category WHERE category_id = :category ORDER BY " + NewsOrder.NEW.getQueryPaged())
                .setParameter("category", category.getId());


        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return news;
    }

    @Override
    public List<News> getNewsByCategoryTop(int page, Category category, Long loggedUser, TimeConstraint timeConstraint) {
        Query query = entityManager.createNativeQuery("SELECT news_id FROM news f NATURAL JOIN news_category WHERE " +
                        " category_id = :category AND creation_date >= " + timeConstraint.getMinimumDateQuery()+ "ORDER BY " + NewsOrder.TOP.getQueryPaged())
                .setParameter("category", category.getId());


        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return news;
    }

    @Override
    @Transactional
    public void deleteNews(News news) {
        entityManager.remove(news);
    }


    @Override
    public int getTotalPagesCategoryNew(Category category) {
        long elemCount = entityManager.createQuery("SELECT count(f) from News f WHERE :category MEMBER OF f.categories",Long.class)
                .setParameter("category", category).getSingleResult();

        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public int getTotalPagesCategoryTop(Category category, TimeConstraint timeConstraint) {
        long elemCount = entityManager.createNativeQuery("SELECT count(*) from news NATURAL JOIN news_category f WHERE category = :category AND" +
                        " creation_date >= " + timeConstraint.getMinimumDateQuery())
                .setParameter("category", category).getFirstResult();

        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public void saveNews(News news, User user) {
        user.getSavedNews().add(new Saved(news, user.getId()));
    }


    private List<News> getNewsOfPage(Query query, int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = JpaUtils.getIdsOfPage(query, page, pageSize);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        final TypedQuery<News> typedQuery = entityManager.createQuery("SELECT f from News f WHERE f.newsId IN :ids",News.class)
                .setParameter("ids", ids);

        List<News> news = typedQuery.getResultList();
        Map<Long, News> map = new HashMap<>();
        for (News news1 : news) {
            map.put(news1.getNewsId(), news1);
        }
        // map id -> news
        news =  ids.stream().map(id -> map.get(id)).collect(Collectors.toList());
        return news;
    }


    @Override
    public Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, Long loggedUser, ProfileCategory profileCategory) {
        page = Math.max(page, 1);
        return profileFunctions.get(profileCategory).getNews(page, user, ns, loggedUser);
    }

    @Override
    public void removeSaved(News news, User user) {
        user.getSavedNews().remove(new Saved(news, user.getId()));

    }

    @Override
    public List<News> getRecommendationNew(int page, User user) {
        Query query = entityManager.createNativeQuery("SELECT news_id FROM\n" +
                                "((SELECT news_id, 1 as priority, accesses, creation_date FROM news f JOIN follows\n" +
                                "                        ON (:userId = follows.user_id AND follows.follows=f.creator )\n" +
                                "                        ORDER BY creation_date DESC LIMIT :limit)\n" +
                                "UNION\n" +
                                "(SELECT news_id, 2 as priority, accesses, creation_date FROM news f WHERE\n" +
                                "                        creator IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                                "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                                "                        ORDER BY creation_date DESC LIMIT :limit)\n" +
                                "UNION\n" +
                                "(SELECT news_id, 3 as priority, accesses, creation_date FROM news f WHERE\n" +
                                "                       creator NOT IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                                "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                                "                        ORDER BY creation_date DESC LIMIT :limit)\n" +
                                "ORDER BY priority, " + NewsOrder.NEW.getQueryPaged() + "  ) AS news"
                ).setParameter("userId", user.getId())
                .setParameter("limit", 5);

        return getNewsOfPage(query, page, PAGE_SIZE);
    }

    @Override
    public List<News> getRecommendationTop(int page, User user, TimeConstraint timeConstraint) {
        Query query = entityManager.createNativeQuery("SELECT news_id FROM\n" +
                        "((SELECT news_id, 1 as priority, accesses, creation_date FROM news f JOIN follows\n" +
                        "                        ON (:userId = follows.user_id AND follows.follows=f.creator )\n" +
                         "WHERE creation_date >= " + timeConstraint.getMinimumDateQuery() +
                        "                        ORDER BY creation_date DESC LIMIT :limit)\n" +
                        "UNION\n" +
                        "(SELECT news_id, 2 as priority, accesses, creation_date FROM news f WHERE\n" +
                        "                        creator IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                        "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                        "AND creation_date >= " + timeConstraint.getMinimumDateQuery() +
                        "                        ORDER BY creation_date DESC LIMIT :limit)\n" +
                        "UNION\n" +
                        "(SELECT news_id, 3 as priority, accesses, creation_date FROM news f WHERE\n" +
                        "                       creator NOT IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                        "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                        "AND creation_date >= " + timeConstraint.getMinimumDateQuery() +

                        "                        ORDER BY creation_date DESC LIMIT :limit)\n" +
                        "ORDER BY priority, " + NewsOrder.TOP.getQueryPaged() + "  ) AS news"
                ).setParameter("userId", user.getId())
                .setParameter("limit", 5);

        return getNewsOfPage(query, page, PAGE_SIZE);
    }

    @Override
    public int getRecommendationNewsPageCountTop(User user, TimeConstraint timeConstraint) {

        long elemCount =   ((Number)entityManager.createNativeQuery("SELECT count(*) FROM ((SELECT news_id FROM news JOIN follows\n" +
                        "                        ON (:userId = follows.user_id AND follows.follows=news.creator )\n" +
                        "AND creation_date >= " + timeConstraint.getMinimumDateQuery() +
                        "                    ORDER BY creation_date DESC LIMIT :limit)\n" +
                        "\n" +
                        "                    UNION\n" +
                        "                        (SELECT news_id FROM news f WHERE\n" +
                        "                        creator IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                        "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                        "AND creation_date >= " + timeConstraint.getMinimumDateQuery() +
                        "                         ORDER BY creation_date DESC LIMIT :limit\n" +
                        "                        )\n" +
                        "                        UNION\n" +
                        "                        (SELECT news_id FROM news f WHERE\n" +
                        "                        creator NOT IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                        "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                        "AND creation_date >= " + timeConstraint.getMinimumDateQuery() +
                        "                         ORDER BY creation_date DESC LIMIT :limit\n" +
                        "                        )) AS ids"
                     ).setParameter("userId", user.getId())
                .setParameter("limit", 5).getSingleResult()).longValue();
        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public int getRecommendationNewsPageCountNew(User user) {

        long elemCount =   ((Number)entityManager.createNativeQuery("SELECT count(*) FROM ((SELECT news_id FROM news JOIN follows\n" +
                        "                        ON (:userId = follows.user_id AND follows.follows=news.creator )\n" +
                        "                    ORDER BY creation_date DESC LIMIT :limit)\n" +
                        "\n" +
                        "                    UNION\n" +
                        "                        (SELECT news_id FROM news f WHERE\n" +
                        "                        creator IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                        "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                        "                         ORDER BY creation_date DESC LIMIT :limit\n" +
                        "                        )\n" +
                        "                        UNION\n" +
                        "                        (SELECT news_id FROM news f WHERE\n" +
                        "                        creator NOT IN (SELECT creator FROM news f NATURAL JOIN upvotes WHERE upvote=true AND user_id=:userId)\n" +
                        "                        AND creator NOT IN(SELECT follows FROM follows WHERE user_id=:userId)\n" +
                        "                         ORDER BY creation_date DESC LIMIT :limit\n" +
                        "                        )) AS ids"
                ).setParameter("userId", user.getId())
                .setParameter("limit", 5).getSingleResult()).longValue();
        return Page.getPageCount(elemCount, PAGE_SIZE);
    }


    Page<News> getAllNewsFromUser(int page, User user, NewsOrder ns, Long loggedUser) {

        int totalPages = getTotalPagesNewsFromUser(user);
        page = Math.min(page, totalPages);

        Query query  = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE creator = :userId order by " + ns.getQueryPaged())
                .setParameter("userId", user.getId());
        List<News> news = getNewsOfPage(query, page, PROFILE_PAGE_SIZE);

        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return new Page<>(news, page, totalPages);
    }

    Page<News> getSavedNews(int page, User user, NewsOrder ns, Long loggedUser) {

        int totalPages = getTotalPagesNewsFromUser(user);
        page = Math.min(page, totalPages);

        Query query = entityManager.createNativeQuery("SELECT news_id FROM saved_news NATURAL JOIN news f WHERE user_id = :userId order by " + ns.getQueryPaged())
                .setParameter("userId", user.getId());
        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return new Page<>(news, page, totalPages);
    }



    private Page<News> getNewsWithRatingFromUser(int page, User user, NewsOrder ns, Long loggedUser, boolean upvote) {

        int totalPages = getTotalPagesNewsFromUserRating(user.getId(), upvote);
        page = Math.min(page, totalPages);

        Query query = entityManager.createNativeQuery("SELECT news_id FROM upvotes NATURAL JOIN news f WHERE upvote = :value AND user_id = :userId order by " + ns.getQueryPaged())
                .setParameter("value", upvote).setParameter("userId", user.getId());
        List<News> news = getNewsOfPage(query, page, PAGE_SIZE);
        if (loggedUser != null)
            news.forEach(n -> n.setUserSpecificVariables(loggedUser));

        return new Page<>(news, page, totalPages);
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
