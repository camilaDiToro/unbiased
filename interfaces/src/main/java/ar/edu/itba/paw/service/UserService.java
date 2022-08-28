package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);
    User create(String email);
    Optional<User> findByEmail(String email);
}
