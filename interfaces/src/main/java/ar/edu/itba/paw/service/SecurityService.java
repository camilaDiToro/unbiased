package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;

import java.util.Optional;

public interface SecurityService {

    Optional<User> getCurrentUser();
}
