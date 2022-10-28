package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Primary
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
        User user = getUserById(id).get();
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
        List<Long> ids = (List<Long>) idQuery.getResultList().stream()
                .map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if(ids.isEmpty())
            return new ArrayList<>();

        List<User> list =  entityManager.createQuery("FROM User WHERE userId IN :ids",
                User.class).setParameter("ids", ids).getResultList();

        return list;

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
        User user = getUserById(userId).get();
        user.getFollowing().add(new Follow(userId, follows));
        entityManager.persist(user);
    }

    @Override
    public User merge(User user) {
        return entityManager.merge(user);
    }

    @Override
    public void unfollow(long userId, long follows) {
        User user = getUserById(userId).get();
        user.getFollowing().remove(new Follow(userId, follows));
        entityManager.persist(user);
    }

    @Override
    public boolean isFollowing(long userId, long followId) {
        User user = getUserById(userId).get();
        return user.getFollowing().contains(new Follow(userId, followId));
    }

    @Override
    public Page<User> searchUsers(int page, String search) {
        page = Math.max(page, 1);

        int totalPages = getTotalPagesSearchUsers(search);
        page = Math.min(page, totalPages);

        Query queryObj = entityManager.createNativeQuery("SELECT user_id FROM users u WHERE (LOWER(u.username) LIKE :query or LOWER(u.email) LIKE :query) and u.status != 'UNABLE' LIMIT :pageSize OFFSET :offset")
                .setParameter("query", "%" + search.toLowerCase() + "%");

        List<User> users = getUsersOfPage(queryObj, page, SEARCH_PAGE_SIZE);

        return new Page<>(users, page,totalPages);
    }

    @Override
    public Page<User> getAdmins(int page, String search) {

        page = Math.max(page, 1);
        search = search == null ? "" : search;
        int totalPages = getTotalPagesGetAdmins(search);
        page = Math.min(page, totalPages);

        Query queryObj = entityManager.createNativeQuery("SELECT user_id FROM users u NATURAL JOIN user_role WHERE (LOWER(u.username) LIKE :query or LOWER(u.email) LIKE :query) " +
                "and u.status != 'UNABLE' and user_role.user_role = 'ROLE_ADMIN' LIMIT :pageSize OFFSET :offset").setParameter("query", "%" + search.toLowerCase() + "%");;

        List<User> users = getUsersOfPage(queryObj, page, SEARCH_PAGE_SIZE);
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

    private List<User> getUsersOfPage(Query query,int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = JpaUtils.getIdsOfPage(query, page, pageSize);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        final TypedQuery<User> typedQuery = entityManager.createQuery("FROM User u WHERE u.userId IN :ids",User.class)
                .setParameter("ids", ids);

        List<User> users = typedQuery.getResultList();
        Map<Long, User> map = new HashMap<>();
        for (User user : users) {
            map.put(user.getUserId(), user);
        }
        // map id -> user
        users =  ids.stream().map(id -> map.get(id)).collect(Collectors.toList());
        return users;
    }

    private int getTotalPagesSearchUsers(String search){
        long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE (LOWER(u.username) LIKE :query or LOWER(u.email) LIKE :query) and u.status != 'UNABLE'", Long.class)
                .setParameter("query", "%" + search.toLowerCase() + "%").getSingleResult();
        return Page.getPageCount(count, SEARCH_PAGE_SIZE);
    }

    private int getTotalPagesGetAdmins(String search){
        BigInteger count = (BigInteger) entityManager.createNativeQuery("SELECT count(distinct user_id) FROM users u NATURAL JOIN user_role WHERE (LOWER(u.username) LIKE :query or LOWER(u.email) LIKE :query) " +
                        "and u.status != 'UNABLE' and user_role.user_role = 'ROLE_ADMIN'")
                        .setParameter("query", "%" + search.toLowerCase() + "%").getResultList().get(0);
        return Page.getPageCount(count.longValue(), SEARCH_PAGE_SIZE);
    }
}
