package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);
    Optional<User> getRegisteredUserById(long id);
    User create(User.UserBuilder userBuilder);
    User createIfNotExists(User.UserBuilder userBuilder);
    Optional<User> findByEmail(String email);

    List<User> getTopCreators(int qty);

    VerificationToken.Status verifyUserEmail(String token);
    VerificationToken.Status resendEmailVerification(String email);
    void addRole(long userId, Role role);
    List<String> getRoles(long userId);
    void updateProfile(long userId, String username, Long imageId);
    Optional<User> findByUsername(String username);
    boolean isFollowing(long followId);

    void followUser(long followId);

    void unfollowUser(long followId);
}
