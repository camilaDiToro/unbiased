package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.Follow;
import ar.edu.itba.paw.model.user.Upvote;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Primary
@Repository
public class UserJpaDao implements UserDao{


    @PersistenceContext
    private EntityManager entityManager;

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
        List<User> users = entityManager.createQuery("FROM User u WHERE u.username LIKE :query",
                User.class).setParameter("query", '%' + search + '%').getResultList();
        return new Page<>(users, 1,1);
    }
}
