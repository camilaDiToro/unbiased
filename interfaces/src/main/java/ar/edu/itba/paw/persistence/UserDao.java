package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    Optional<User> getRegisteredUserById(long id);
    User create(User.UserBuilder userBuilder);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    void verifyEmail(long id);
    List<User> getTopCreators(int qty);
    void updateImage(User user, Image newImage, Image oldImage);
    void addFollow(long userId, long follows);
    void unfollow(long userId, long follows);
    boolean isFollowing(long userId, long followId);
    Page<User> searchUsers(int page, String search);
    Page<User> getAdmins(int page, String search);
    long getFollowingCount(long userId);
    long getFollowersCount(long userId);
    boolean pingNewsToggle(User user, News news);
    List<User> getFollowersWithEmailPublishNewsActive(User user);
}
