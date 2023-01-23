package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.MailOption;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import java.util.Collection;
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
    void updateEmailSettings(User currentUser, Collection<MailOption> options);
    boolean isFollowing(final User currentUser, long userId);
    boolean followUser(final User currentUser, long userId);
    boolean unfollowUser(final User currentUser, long userId);
    void updateEmailSettings(User currentUser, boolean follow, boolean comment, boolean followingPublished, boolean positivityChange);
    Page<User> searchUsers(int page, String search);
    boolean pingNewsToggle(User currentUser, News news);

    void pinNews(User user, News news);

    void setUserImage(long userId, byte[] bytes, String dataType);

    void unpinNews(User user, News news);

    void unpinNews(User user);

    List<User> getFollowing(User user);

    List<User> getFollowedBy(User user);

    ProfileCategory getProfileCategory(Optional<User> maybeCurrentUser, ProfileCategory category, final User profile);
    long getFollowingCount(long userId);
    long getFollowersCount(long userId);
    boolean isUserAdmin(final User user);
}
