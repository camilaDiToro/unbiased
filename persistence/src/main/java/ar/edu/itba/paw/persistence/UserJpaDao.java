package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.Follow;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class UserJpaDao implements UserDao{


    @PersistenceContext
    private EntityManager entityManager;

    private static final int SEARCH_PAGE_SIZE = 15;

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> getRegisteredUserById(long id) {
        final TypedQuery<User> query = entityManager.createQuery("FROM User WHERE userId = :id and status = :status",User.class);
        query.setParameter("id", id).setParameter("status", UserStatus.REGISTERED);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public User create(User.UserBuilder userBuilder) {
        final User user = userBuilder.build();
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = entityManager.createQuery("FROM User AS u WHERE u.email = :email",User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final TypedQuery<User> query = entityManager.createQuery("FROM User WHERE username = :username",User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void verifyEmail(long id) {
        final User user = getUserById(id).get();
        user.setStatus(UserStatus.REGISTERED);
        entityManager.persist(user);
    }

    @Override
    public List<User> getTopCreators(int qty) {

        final Query idQuery = entityManager.createNativeQuery(
                "WITH interactions AS (SELECT creator AS user_id, count(*) AS interaction_count FROM upvotes JOIN news ON upvotes.news_id = news.news_id " +
                        "WHERE DATE(interaction_date) = CURRENT_DATE GROUP BY creator LIMIT :limit) " +
                        "SELECT users.user_id FROM interactions NATURAL JOIN users ORDER BY interaction_count DESC")
                .setParameter("limit", qty);

        @SuppressWarnings("unchecked")
        final List<Long> orderedIds = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if(orderedIds.isEmpty())
            return new ArrayList<>();

        final List<User> unorderedUserList =  entityManager.createQuery("FROM User WHERE userId IN :ids",
                User.class).setParameter("ids", orderedIds).getResultList();

        final Map<Long, User> idToUserMap = new HashMap<>();

        unorderedUserList.forEach(u -> idToUserMap.put(u.getId(), u));

        final List<User> orderedList = orderedIds.stream().map(idToUserMap::get).collect(Collectors.toList());

        return orderedList;

    }

    @Override
    public void updateImage(User user, Image newImage, Image oldImage) {
        user.setImage(newImage);
        entityManager.persist(user);
        if(oldImage!=null){
            oldImage = entityManager.merge(oldImage);
            entityManager.remove(oldImage);
        }
    }

    @Override
    public void addFollow(long userId, long follows) {
        entityManager.persist(new Follow(userId, follows));
    }

    @Override
    public void unfollow(long userId, long follows) {
        final User user = getUserById(userId).get();
        user.getFollowing().remove(new Follow(userId, follows));
    }

    @Override
    public boolean isFollowing(long userId, long followId) {
        final User user = getUserById(userId).get();
        return user.getFollowing().contains(new Follow(userId, followId));
    }

    @Override
    public Page<User> searchUsers(int page, String search) {
        page = Math.max(page, 1);

        final int totalPages = getTotalPagesSearchUsers(search);
        page = Math.min(page, totalPages);

        final Query queryObj = entityManager.createNativeQuery("SELECT user_id FROM users u WHERE (LOWER(u.username) LIKE :query escape '\\'  or LOWER(u.email) LIKE :query escape '\\' ) and u.status != 'UNABLE' LIMIT :pageSize OFFSET :offset")
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(search.toLowerCase()) + "%");

        final List<User> users = getUsersOfPage(queryObj, page, SEARCH_PAGE_SIZE);

        return new Page<>(users, page,totalPages);
    }

    @Override
    public Page<User> getAdmins(int page, String search) {

        page = Math.max(page, 1);
        search = search == null ? "" : search;
        final int totalPages = getTotalPagesGetAdmins(search);
        page = Math.min(page, totalPages);

        final Query queryObj = entityManager.createNativeQuery("SELECT user_id FROM users u NATURAL JOIN user_role WHERE (LOWER(u.username) LIKE :query escape '\\'  or LOWER(u.email) LIKE :query escape '\\' ) " +
                "and u.status != 'UNABLE' and user_role.user_role = 'ROLE_ADMIN' LIMIT :pageSize OFFSET :offset").setParameter("query", "%" + JpaUtils.escapeSqlLike(search.toLowerCase()) + "%");

        final List<User> users = getUsersOfPage(queryObj, page, SEARCH_PAGE_SIZE);
        return new Page<>(users, page,totalPages);
    }

    @Override
    public void pingNewsToggle(User user, News news) {
        if (news.equals(user.getPingedNews())) {
            user.setPingedNews(null);
        } else {
            user.setPingedNews(news);
        }
    }

    @Override
    public List<User> getFollowersWithEmailPublishNewsActive(User user) {
        @SuppressWarnings("unchecked")
        final List<Long> ids = (List<Long>) entityManager.createNativeQuery("select user_id from users where user_id IN ( " +
                        "select follows.user_id from follows join email_settings on follows.user_id = email_settings.user_id  where follows= :userId and following_published=true)")
                .setParameter("userId", user.getId()).getResultList().stream().map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()){
            return Collections.emptyList();
        }
        return entityManager.createQuery("FROM User u WHERE u.userId IN :ids",User.class)
                .setParameter("ids", ids).getResultList();
    }
    @Override
    public long getFollowingCount(long userId) {
        return entityManager.createQuery("SELECT COUNT(follows) FROM Follow WHERE userId = :id", Long.class).setParameter("id", userId).getSingleResult();
    }

    @Override
    public long getFollowersCount(long userId) {
        return entityManager.createQuery("SELECT COUNT(userId) FROM Follow WHERE follows = :id", Long.class).setParameter("id", userId).getSingleResult();
    }

    private List<User> getUsersOfPage(Query query,int page, int pageSize) {
        @SuppressWarnings("unchecked")
        final List<Long> ids = JpaUtils.getIdsOfPage(query, page, pageSize);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        final TypedQuery<User> typedQuery = entityManager.createQuery("FROM User u WHERE u.userId IN :ids",User.class)
                .setParameter("ids", ids);

        final List<User> unorderedUsers = typedQuery.getResultList();
        final Map<Long, User> usersMapById = new HashMap<>();
        for (User user : unorderedUsers) {
            usersMapById.put(user.getUserId(), user);
        }
        // map id -> user
        final List<User> orderedUsers =  ids.stream().map(id -> usersMapById.get(id)).collect(Collectors.toList());
        return orderedUsers;
    }

    private int getTotalPagesSearchUsers(String search){
        final long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE (LOWER(u.username) LIKE :query  escape '' or LOWER(u.email) LIKE :query  escape '\\') and u.status <> 'UNABLE'", Long.class)
                .setParameter("query", "%" + JpaUtils.escapeSqlLike(search.toLowerCase()) + "%").getSingleResult();
        return Page.getPageCount(count, SEARCH_PAGE_SIZE);
    }

    private int getTotalPagesGetAdmins(String search){
        final BigInteger count = (BigInteger) entityManager.createNativeQuery("SELECT count(distinct user_id) FROM users u NATURAL JOIN user_role WHERE (LOWER(u.username) LIKE :query escape '\\'  or LOWER(u.email) LIKE :query  escape '\\') " +
                        "and u.status != 'UNABLE' and user_role.user_role = 'ROLE_ADMIN'")
                        .setParameter("query", "%" + JpaUtils.escapeSqlLike(search.toLowerCase()) + "%").getResultList().get(0);
        return Page.getPageCount(count.longValue(), SEARCH_PAGE_SIZE);
    }
}
