package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long id);
    User create(String username, String password);
    Optional<User> findByUsername(String username);
}
