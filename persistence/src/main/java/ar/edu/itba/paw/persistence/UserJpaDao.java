package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // Not implemented yet
    }

    @Override
    public List<User> getTopCreators(int qty) {
        return null;
    }

    @Override
    public void updateUsername(User user, String username) {
        // Should be done by updating the entity
    }

    @Override
    public void updateImage(User user, Long imageId) {
        // Should be done by updating the entity
    }

    @Override
    public void addFollow(long userId, long follows) {
        // Not implemented yet
    }

    @Override
    public void unfollow(long userId, long follows) {
        // Not implemented yet
    }

    @Override
    public boolean isFollowing(long userId, long followId) {
        // Not implemented yet
        return false;
    }

    @Override
    public Page<User> searchUsers(int page, String search) {
        return new Page<>(new ArrayList<>(), 1,1);
    }
}
