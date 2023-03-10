package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Saved;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.functional.GetNewsProfileFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public List<News> getNewNews(int page, String query) {
        final Query queryObj = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE LOWER(title) LIKE :query escape '\\'  ORDER BY " + NewsOrder.NEW.getQueryPaged())
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(query.toLowerCase()) + "%");

        final List<News> news = getNewsOfPage(queryObj, page, SEARCH_PAGE_SIZE);

        return news;
    }



    @Override
    public List<News> getTopNews(int page, String query, TimeConstraint timeConstraint) {
        final Query queryObj = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE LOWER(title) LIKE :query escape '\\'  AND creation_date >= "+timeConstraint.getMinimumDateQuery() + " ORDER BY " + NewsOrder.TOP.getQueryPaged())
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(query.toLowerCase()) + "%");

        List<News> news = getNewsOfPage(queryObj, page, SEARCH_PAGE_SIZE);

        return news;
    }


    @Override
    public int getTotalPagesAllNews(String query, TimeConstraint timeConstraint) {
        final Number elemCount = (Number) entityManager.createNativeQuery("SELECT count(*) FROM news f WHERE LOWER(title) LIKE :query escape '\\'  AND creation_date >= " +timeConstraint.getMinimumDateQuery())
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(query.toLowerCase()) + "%").getSingleResult();

        return Page.getPageCount(elemCount.longValue(), PAGE_SIZE);
    }


    @Override
    public Optional<News> getById(long id) {
        final TypedQuery<News> typedQuery = entityManager.createQuery("SELECT f from News f WHERE f.newsId = :newsId ",News.class).setParameter("newsId", id);
        final Optional<News> news = typedQuery.getResultList().stream().findFirst();

        return news;
    }




    @Override
    public List<News> getNewsByCategoryNew(int page, Category category) {
        final Query query = entityManager.createNativeQuery("SELECT news_id FROM news f NATURAL JOIN news_category WHERE category_id = :category ORDER BY " + NewsOrder.NEW.getQueryPaged())
                .setParameter("category", category.getId());


        final List<News> news = getNewsOfPage(query, page, PAGE_SIZE);

        return news;
    }



    @Override
    public List<News> getNewsByCategoryTop(int page, Category category, TimeConstraint timeConstraint) {
        final Query query = entityManager.createNativeQuery("SELECT news_id FROM news f NATURAL JOIN news_category WHERE " +
                        " category_id = :category AND creation_date >= " + timeConstraint.getMinimumDateQuery()+ "ORDER BY " + NewsOrder.TOP.getQueryPaged())
                .setParameter("category", category.getId());


        final List<News> news = getNewsOfPage(query, page, PAGE_SIZE);

        return news;
    }

    @Override
    public void deleteNews(News news) {
        entityManager.remove(news);
    }


    @Override
    public int getTotalPagesCategoryNew(Category category) {
        final long elemCount = entityManager.createQuery("SELECT count(f) from News f WHERE :category MEMBER OF f.categories",Long.class)
                .setParameter("category", category).getSingleResult();

        return Page.getPageCount(elemCount, PAGE_SIZE);
    }

    @Override
    public int getTotalPagesCategoryTop(Category category, TimeConstraint timeConstraint) {
        final Number elemCount = (Number) entityManager.createNativeQuery("SELECT count(f) from news NATURAL JOIN news_category f WHERE category_id = :category AND" +
                        " creation_date >= " + timeConstraint.getMinimumDateQuery())
                .setParameter("category", category.getId()).getSingleResult();

        return Page.getPageCount(elemCount.longValue(), PAGE_SIZE);
    }

    @Override
    public void saveNews(News news, User user) {
        user.getSavedNews().add(new Saved(news, user.getId()));
    }


    private List<News> getNewsOfPage(Query query, int page, int pageSize) {
        @SuppressWarnings("unchecked")
        final List<Long> ids = JpaUtils.getIdsOfPage(query, page, pageSize);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        final TypedQuery<News> typedQuery = entityManager.createQuery("SELECT f from News f WHERE f.newsId IN :ids",News.class)
                .setParameter("ids", ids);

        List<News> unorderedNews = typedQuery.getResultList();
        Map<Long, News> map = new HashMap<>();
        for (News news1 : unorderedNews) {
            map.put(news1.getNewsId(), news1);
        }
        List<News> orderedNews = ids.stream().map(id -> map.get(id)).collect(Collectors.toList());
        return orderedNews;
    }


    @Override
    public Page<News> getNewsFromProfile(int page, User user, NewsOrder ns, ProfileCategory profileCategory) {
        page = Math.max(page, 1);
        return profileFunctions.get(profileCategory).getNews(page, user, ns);
    }

    @Override
    public void removeSaved(News news, User user) {
        user.getSavedNews().remove(new Saved(news, user.getId()));
    }

    @Override
    public List<News> getRecommendationNew(int page, User user) {
        final Query query = entityManager.createNativeQuery("SELECT news_id FROM\n" +
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
        final Query query = entityManager.createNativeQuery("SELECT news_id FROM\n" +
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

        final long elemCount =   ((Number)entityManager.createNativeQuery("SELECT count(*) FROM ((SELECT news_id FROM news JOIN follows\n" +
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
    public CategoryStatistics getCategoryStatistics(long userId) {
        final Object[] results = (Object[]) entityManager.createNativeQuery("SELECT coalesce(sum( case when category_id = :tourismCat then 1 else 0 end),0) as tourism, " +
                "                                                           coalesce(sum( case when category_id = :showCat then 1 else 0 end),0) as show, " +
                "                                                           coalesce(sum( case when category_id = :politicsCat then 1 else 0 end),0) as politics, " +
                "                                                           coalesce(sum( case when category_id = :economicsCat then 1 else 0 end),0) as economics, " +
                "                                                           coalesce(sum( case when category_id = :sportCat then 1 else 0 end),0) as sport, " +
                "                                                           coalesce(sum( case when category_id = :techCat then 1 else 0 end),0) as tech, " +
                "                                                           count(*) as all, " +
                "                                                           coalesce(sum( case when category_id is null then 1 else 0 end),0) " +
                "from ( select * from news where creator = :userId ) as n natural left join news_category ")
                .setParameter("userId", userId)
                .setParameter("tourismCat", Category.TOURISM.ordinal())
                .setParameter("showCat", Category.SHOW.ordinal())
                .setParameter("politicsCat", Category.POLITICS.ordinal())
                .setParameter("economicsCat", Category.ECONOMICS.ordinal())
                .setParameter("sportCat", Category.SPORTS.ordinal())
                .setParameter("techCat", Category.TECHNOLOGY.ordinal())
                .getSingleResult();

        return new CategoryStatistics(((BigInteger) results[0]).longValue(), ((BigInteger) results[1]).longValue(),((BigInteger) results[2]).longValue(),
                ((BigInteger) results[3]).longValue(),((BigInteger) results[4]).longValue(),((BigInteger) results[5]).longValue(),((BigInteger) results[6]).longValue(),
                (((BigInteger) results[7]).longValue()));
    }

    @Override
    public int getRecommendationNewsPageCountNew(User user) {

        final long elemCount =   ((Number)entityManager.createNativeQuery("SELECT count(*) FROM ((SELECT news_id FROM news JOIN follows\n" +
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


    private Page<News> getAllNewsFromUser(int page, User user, NewsOrder ns) {

        final int totalPages = getTotalPagesNewsFromUser(user);
        page = Math.min(page, totalPages);

        final Query query  = entityManager.createNativeQuery("SELECT news_id FROM news f WHERE creator = :userId order by " + ns.getQueryPaged())
                .setParameter("userId", user.getId());
        final List<News> news = getNewsOfPage(query, page, PROFILE_PAGE_SIZE);



        return new Page<>(news, page, totalPages);
    }

    private Page<News> getSavedNews(int page, User user, NewsOrder ns) {

        final int totalPages = getTotalPagesNewsFromUserSaved(user);
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery("SELECT news_id FROM saved_news NATURAL JOIN news f  WHERE user_id = :userId order by " + ns.getQueryPaged())
                .setParameter("userId", user.getId());
        final List<News> news = getNewsOfPage(query, page, PROFILE_PAGE_SIZE);


        return new Page<>(news, page, totalPages);
    }



    private Page<News> getNewsWithRatingFromUser(int page, User user, NewsOrder ns,  boolean upvote) {
        final int totalPages = getTotalPagesNewsFromUserRating(user.getId(), upvote);
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery("SELECT news_id FROM upvotes NATURAL JOIN news f WHERE upvote = :value AND user_id = :userId order by " + ns.getQueryPaged())
                .setParameter("value", upvote).setParameter("userId", user.getId());
        final List<News> news = getNewsOfPage(query, page, PROFILE_PAGE_SIZE);


        return new Page<>(news, page, totalPages);
    }

    private Page<News> getNewsUpvotedByUser(int page, User user, NewsOrder ns) {
        return getNewsWithRatingFromUser(page, user, ns,  true);
    }

    private Page<News> getNewsDownvotedByUser(int page, User user, NewsOrder ns) {
        return getNewsWithRatingFromUser(page, user, ns,  false);

    }

    private int getTotalPagesNewsFromUser(User user) {
        final Long elemCount =  entityManager.createQuery("SELECT count(f) from News f WHERE f.creator = :user ",Long.class)
                .setParameter("user", user) .getSingleResult();
        return Page.getPageCount(elemCount, PROFILE_PAGE_SIZE);
    }


    private int getTotalPagesNewsFromUserRating(long userId, boolean upvoted) {
        final Long elemCount =  entityManager.createQuery("SELECT count(u) from Upvote u WHERE u.userId = :user AND u.value = :value",Long.class)
                .setParameter("user", userId)
                .setParameter("value", upvoted).getSingleResult();
        return Page.getPageCount(elemCount, PROFILE_PAGE_SIZE);
    }

    @Override
    public Page<News> getReportedNews(int page, ReportOrder reportOrder) {

        page = Math.min(page,1);
        final int totalPages = getTotalReportedNews();
        page = Math.min(page, totalPages);

        final Query idsQuery = entityManager.createNativeQuery(
                        "SELECT news_id FROM report n NATURAL JOIN news GROUP BY news_id ORDER BY "
                                + reportOrder.getQuery() +" LIMIT :limit OFFSET :offset")
                .setParameter("limit",PAGE_SIZE)
                .setParameter("offset",(page-1)*PAGE_SIZE);

        final TypedQuery<News> objectQuery = entityManager.createQuery("SELECT n from News n WHERE n.newsId IN :ids ", News.class);

        return JpaUtils.getPage(page, totalPages, idsQuery, objectQuery, News::getNewsId);
    }

    @Override
    public Page<News> getReportedByUserNews(int page, long userId) {

        page = Math.min(page,1);
        final int totalPages = getTotalReportedByUserNews(userId);
        page = Math.min(page, totalPages);

        final Query idsQuery = entityManager.createNativeQuery("SELECT news_id FROM report n WHERE user_id = :userId LIMIT :limit OFFSET :offset")
                .setParameter("userId", userId)
                .setParameter("limit",PAGE_SIZE)
                .setParameter("offset",(page-1)*PAGE_SIZE);

        final TypedQuery<News> objectQuery = entityManager.createQuery("SELECT n from News n WHERE n.newsId IN :ids ", News.class);

        return JpaUtils.getPage(page, totalPages, idsQuery, objectQuery, News::getNewsId);
    }

    private int getTotalReportedByUserNews(long userId) {
        final long count = entityManager.createQuery("SELECT COUNT(distinct r.news) FROM ReportDetail r WHERE r.reporter.userId = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return Page.getPageCount(count, PAGE_SIZE);
    }

    @Override
    public Optional<News> getPinnedByUserNews(long userId) {
        Query query = entityManager.createNativeQuery("SELECT pinged_news FROM users n WHERE user_id = :userId")
                .setParameter("userId", userId);
        @SuppressWarnings("unchecked")
        final Object maybeId = query.getSingleResult();

        if (maybeId == null)
            return Optional.empty();

        return Optional.of(entityManager.createQuery("SELECT n from News n WHERE n.newsId = :id", News.class).setParameter("id", ((Number) maybeId).longValue()).getSingleResult());
    }

    private int getTotalPagesNewsFromUserSaved(User user) {
        final int elemCount =  entityManager.createQuery("SELECT u.savedNews.size from User u WHERE u.userId = :user ",Integer.class)
                .setParameter("user", user.getId())
                .getSingleResult();
        return Page.getPageCount(elemCount, PROFILE_PAGE_SIZE);
    }

    @Override
    public ReportDetail reportNews(News news, User reporter, ReportReason reportReason) {
        final ReportDetail reportDetail = new ReportDetail(news, reporter, LocalDateTime.now(), reportReason);
        entityManager.persist(reportDetail);
        LOGGER.debug("News {} with id {} reported. The reason is {}", news.getTitle(), news.getNewsId(), reportReason.getDescription());
        return reportDetail;
    }

    @Override
    public boolean isReportedByUser(final News news,final User user){
        int value = ((Number)entityManager.createNativeQuery("SELECT count(*) FROM report WHERE user_id = :userId and news_id = :newsId")
                     .setParameter("userId", user.getId()).setParameter("newsId", news.getNewsId())
                     .getSingleResult()).intValue();
        return value >= 1;
    }

    @Override
    public boolean isSavedByUser(long newsId, long userId) {
        int value = ((Number) entityManager.createNativeQuery("SELECT count(*) FROM saved_news WHERE user_id = :userId and news_id = :newsId")
                .setParameter("userId", userId).setParameter("newsId", newsId)
                .getSingleResult()).intValue();
        return value >= 1;
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, long newsId) {
        page = Math.min(page,1);
        final int totalPages =getTotalReportsFromNews(newsId);
        page = Math.min(page, totalPages);

        Query idsQuery = entityManager.createNativeQuery(
                        "SELECT id FROM report where news_id = :newsId" +
                                " LIMIT :limit OFFSET :offset")
                .setParameter("newsId", newsId)
                .setParameter("limit",PAGE_SIZE)
                .setParameter("offset",(page-1)*PAGE_SIZE);

        final TypedQuery<ReportDetail> objectQuery = entityManager.createQuery(
                "SELECT n from ReportDetail n WHERE n.id IN :ids " , ReportDetail.class);

        return JpaUtils.getPage(page, totalPages , idsQuery, objectQuery, ReportDetail::getId);
    }

    private int getTotalReportsFromNews(long newsId) {
        final long count = entityManager.createQuery("SELECT COUNT(distinct r.id) FROM ReportDetail r WHERE r.news.id = :newsId", Long.class)
                .setParameter("newsId", newsId)
                .getSingleResult();
        return Page.getPageCount(count, PAGE_SIZE);
    }


    private int getTotalReportedNews() {
        final long count = entityManager.createQuery("SELECT COUNT(distinct r.news) FROM ReportDetail r", Long.class)
                .getSingleResult();
        return Page.getPageCount(count, PAGE_SIZE);
    }
}
