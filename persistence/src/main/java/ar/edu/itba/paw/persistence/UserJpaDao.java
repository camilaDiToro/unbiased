package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.Follow;
import ar.edu.itba.paw.model.user.Upvote;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        final TypedQuery<User> query = entityManager.createQuery("from User as u WHERE u.email = :email",User.class);
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
        List<User> list =  entityManager.createQuery("SELECT u FROM User u",
                User.class).getResultList();
//        list.sort((u1, u2) -> {
//            Predicate<Upvote> predicate = u -> u.getDate().toLocalDateTime().toLocalDate().equals(LocalDate.now());
//            return (int) u1.getUpvoteSet().stream()
//                    .filter(predicate).count() - (int) u2.getUpvoteSet().stream()
//                    .filter(predicate).count();
//        }); TODO: fix

        return list.stream().limit(qty).collect(Collectors.toList());

    }

    @Override
    public void updateUsername(User user, String username) {
        user.setUsername(username);
        entityManager.persist(user);
    }

    @Override
    public void updateImage(User user, Long imageId) {
        user.setImageId(imageId);
        entityManager.persist(user);
    }

    @Override
    public void addFollow(long userId, long follows) {
        // Not implemented yet
        User user = getUserById(userId).get();
        user.getFollowing().add(new Follow(userId, follows));
        entityManager.persist(user);
    }

    @Override
    public void unfollow(long userId, long follows) {
        // Not implemented yet
        User user = getUserById(userId).get();
        user.getFollowing().remove(new Follow(userId, follows));
        // TODO: figure out why set modification is not persisting in database
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
