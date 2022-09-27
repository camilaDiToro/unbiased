package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);
    Optional<User> getRegisteredUserById(long id);
    User create(User.UserBuilder userBuilder);
    Optional<User> findByEmail(String email);
    List<User> getTopCreators(int qty);
    VerificationToken.Status verifyUserEmail(String token);
    VerificationToken.Status resendEmailVerification(String email);
    void addRole(User user, Role role);
    List<String> getRoles(User user);
    void updateProfile(User user, String username, Long imageId);
    Optional<User> findByUsername(String username);
    boolean isFollowing(User user);
    void followUser(User user);
    void unfollowUser(User user);

    Page<User> getUsersByQuery(String query, int page);
}
