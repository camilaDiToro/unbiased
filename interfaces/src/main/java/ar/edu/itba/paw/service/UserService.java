package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
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
    void addRole(long userId, Role role);
    void updateProfile(long userId, String username, byte[] bytes, String dataType, String description);
    Optional<User> findByUsername(String username);
    boolean isFollowing(long userId);
    void followUser(long userId);
    void unfollowUser(long userId);
    Page<User> searchUsers(int page, String search);
    void pingNewsToggle(News news);
    ProfileCategory getProfileCategory(String category, User profile);
    long getFollowingCount(long userId);
    long getFollowersCount(long userId);
    boolean isUserAdmin(User user);
}
