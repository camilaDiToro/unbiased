package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);
    User create(User.UserBuilder userBuilder);
    User createIfNotExists(User.UserBuilder userBuilder);
    Optional<User> findByEmail(String email);
    VerificationToken.Status verifyUserEmail(String token);
    VerificationToken.Status resendEmailVerification(String email);
}
