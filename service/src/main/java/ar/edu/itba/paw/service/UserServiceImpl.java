package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.InvalidFilterException;
import ar.edu.itba.paw.model.user.EmailSettings;
import ar.edu.itba.paw.model.user.MailOption;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final VerificationTokenService verificationTokenService;

    private final ImageService imageService;

    private static final int TOP_CREATORS_COUNT = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(final ImageService imageService, final UserDao userDao, final PasswordEncoder passwordEncoder,final EmailService emailService,
                           final VerificationTokenService verificationTokenService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.imageService = imageService;
    }


    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public Optional<User> getRegisteredUserById(long id) {
        return userDao.getRegisteredUserById(id);
    }

    @Override
    @Transactional
    public User create(User.UserBuilder userBuilder) {
        if(userBuilder.getPass() != null){
            userBuilder.pass(passwordEncoder.encode(userBuilder.getPass()));
        }
        final User user = userDao.create(userBuilder);
        final VerificationToken token = verificationTokenService.newToken(user.getId());
        final Locale locale = LocaleContextHolder.getLocale();
        emailService.sendVerificationEmail(user, token, locale);
        final EmailSettings emailSettings = new EmailSettings(true,true,false,true,locale, user);
        user.setEmailSettings(emailSettings);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional
    public VerificationToken.Status verifyUserEmail(String token) {
        final Optional<VerificationToken> mayBeVt = verificationTokenService.getToken(token);
        if(!mayBeVt.isPresent()){
            LOGGER.info("Trying to validate token {}, but it does not exist", token);
            return VerificationToken.Status.NOT_EXISTS;
        }
        final VerificationToken vt = mayBeVt.get();

        if(!vt.isValidToken()){
            LOGGER.info("Trying to validate token {}, but it has expired", token);
            return VerificationToken.Status.EXPIRED;
        }
        userDao.verifyEmail(vt.getUserId());
        verificationTokenService.deleteEmailToken(vt.getUserId());
        return VerificationToken.Status.SUCCESFFULLY_VERIFIED;
    }


    @Override
    @Transactional
    public VerificationToken.Status resendEmailVerification(User user) {
        if(!user.getStatus().equals(UserStatus.UNABLE)){
            LOGGER.info("Trying to resend verification email to {}, but this email is already registered", user.getEmail());
            return VerificationToken.Status.ALREADY_VERIFIED;
        }

        verificationTokenService.deleteEmailToken(user.getId());
        final VerificationToken token = verificationTokenService.newToken(user.getId());
        final Locale locale = LocaleContextHolder.getLocale();
        emailService.sendVerificationEmail(user, token, locale);
        return VerificationToken.Status.SUCCESSFULLY_RESENDED;
    }

    @Override
    @Transactional
    public void addRole(long userId, Role role) {
        userDao.getUserById(userId).orElseThrow( () -> new UserNotFoundException(userId));
    }

    @Override
    @Transactional
    public void updateProfile(long userId, String username, final byte[] bytes, String dataType, String description) {
        final User user = userDao.getUserById(userId).orElseThrow( () -> new UserNotFoundException(userId));
        if(bytes!=null && bytes.length != 0){
            userDao.updateImage(user, new Image(bytes, dataType), user.getImage());
        }

        if(username!= null && !username.isEmpty()){
            user.setUsername(username);
        }

        if(description!= null && !description.isEmpty()){
           user.setDescription(description);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public boolean followUser(final User currentUser, long userId) {
        if(isFollowing(currentUser,userId)){
            return false;
        }
        final User following = userDao.getUserById(userId).orElseThrow( () -> new UserNotFoundException(userId));
        userDao.addFollow(currentUser.getId(), userId);
        final EmailSettings emailSettings = following.getEmailSettings();
        if(emailSettings!= null && emailSettings.isFollow()){
            emailService.sendNewFollowerEmail(following,currentUser,emailSettings.getLocale());
        }
        return true;
    }

    @Override
    @Transactional
    public boolean unfollowUser(final User currentUser, long userId) {
        if(isFollowing(currentUser,userId)){
            userDao.unfollow(currentUser.getId(), userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void updateEmailSettings(User currentUser, boolean follow, boolean comment, boolean followingPublished, boolean positivityChange) {
        final Locale locale = LocaleContextHolder.getLocale();
        if(currentUser.getEmailSettings() == null){
            currentUser.setEmailSettings(new EmailSettings(follow,comment,followingPublished,positivityChange,locale, currentUser));
            return;
        }
        currentUser.getEmailSettings().setComment(comment);
        currentUser.getEmailSettings().setFollow(follow);
        currentUser.getEmailSettings().setFollowingPublished(followingPublished);
        currentUser.getEmailSettings().setPositivityChange(positivityChange);
    }

    @Override
    @Transactional
    public void updateEmailSettings(User currentUser, Collection<MailOption> options) {
        final Locale locale = LocaleContextHolder.getLocale();
        if(currentUser.getEmailSettings() == null){
            currentUser.setEmailSettings(new EmailSettings(options,locale, currentUser));
            return;
        }
        currentUser.getEmailSettings().setSettings(options);
    }

    @Override
    public boolean isFollowing(final User currentUser, long userId) {
        return userDao.isFollowing(currentUser.getId(), userId);
    }

    @Override
    public Page<User> searchUsers(int page, String search) {
        return userDao.searchUsers(page, search);
    }

    @Override
    public Page<User> getAdmins(int page, String search) {
        return userDao.getAdmins(page,search);
    }

    @Override
    public Page<User> getNotAdmins(int page, String search) {
        return userDao.getNotAdmins(page,search);
    }

    @Override
    public boolean isUserEnabled(String email) {
        Optional<User> mayBeUser = userDao.findByEmail(email);
        if(!mayBeUser.isPresent()){
            return false;
        }
        return mayBeUser.get().getStatus().equals(UserStatus.REGISTERED);
    }

    @Override
    public Page<User> getTopCreators() {
        return new Page<>(userDao.getTopCreators(TOP_CREATORS_COUNT), 1, 1);
    }


    @Override
    @Transactional
    public boolean pingNewsToggle(final User currentUser,final News news) {
        return userDao.pingNewsToggle(currentUser, news);
    }

    @Override
    @Transactional
    public void pinNews(final User user, final News news) {
        userDao.pinNews(user, news);
    }

    @Override
    @Transactional
    public void setUserImage(final long userId, final byte[] bytes,String dataType) {
        final User user = userDao.getUserById(userId).orElseThrow( () -> new UserNotFoundException(userId));
        if(bytes!=null && bytes.length != 0){
            userDao.updateImage(user, new Image(bytes, dataType), user.getImage());
        }
    }

    @Override
    @Transactional
    public void unpinNews(final User user, final News news) {
        userDao.unpinNews(user, news);
    }

    @Override
    @Transactional
    public void unpinNews(final User user) {
        userDao.unpinNews(user);
    }

    @Override
    public Page<User> getFollowing(int page, long userId) {
        userDao.getUserById(userId).orElseThrow(()->new UserNotFoundException(userId));
        return userDao.getFollowing(page, userId);
    }

    @Override
    public List<User> getFollowedBy(User user) {
        return userDao.getFollowedBy(user.getUserId());
    }

    @Override
    public ProfileCategory getProfileCategory(final Optional<User> maybeCurrentUser, ProfileCategory category, final User profile) {


        if (!profile.getRoles().contains(Role.ROLE_JOURNALIST) && category.equals(ProfileCategory.MY_POSTS)){
            throw new InvalidFilterException(String.format("The user %s is not a journalist, so it is not posible to retrieve his posts", profile.toString()));
        }

        if (category.equals(ProfileCategory.SAVED) &&
                !(maybeCurrentUser.isPresent() && maybeCurrentUser.get().equals(profile))){
            throw new InvalidFilterException("Saved articles can just be retrieved by the user who saved them");
        }

        return category;
    }


    @Override
    public long getFollowingCount(long userId) {
        return userDao.getFollowingCount(userId);
    }

    @Override
    public long getFollowersCount(long userId) {
        return userDao.getFollowersCount(userId);
    }

    @Override
    public boolean isUserAdmin(final User user) {
        if(user == null)
            return false;
        final Collection<Role> roles = user.getRoles();
        if (roles == null)
            return false;
        return roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_OWNER);
    }
}
