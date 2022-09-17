package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long id);
    User create(User.UserBuilder userBuilder);
    User createIfNotExists(User.UserBuilder userBuilder);
    Optional<User> findByEmail(String email);
    void verifyEmail(long id);
    void updateUsername(long userId, String username);
    void updateImage(long userId, Long imageId);
}
