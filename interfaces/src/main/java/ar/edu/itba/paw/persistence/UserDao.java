package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long id);
    User create(User.UserBuilder userBuilder);
    User createIfNotExists(User.UserBuilder userBuilder);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    void verifyEmail(long id);

    List<User> getTopCreators(int qty);

    void updateUsername(long userId, String username);
    void updateImage(long userId, Long imageId);
}
