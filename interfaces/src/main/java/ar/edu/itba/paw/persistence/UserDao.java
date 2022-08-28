package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserById(long id);
    User create(String email);
    Optional<User> findByEmail(String email);
}
