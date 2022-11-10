package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.InvalidFilterException;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
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

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    private final SecurityService securityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, EmailService emailService,
                           VerificationTokenService verificationTokenService, SecurityService securityService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.securityService = securityService;
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
        User user = userDao.create(userBuilder);
        final VerificationToken token = verificationTokenService.newToken(user.getId());
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        emailService.sendVerificationEmail(user, token, locale);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional
    public VerificationToken.Status verifyUserEmail(String token) {
        Optional<VerificationToken> mayBeVt = verificationTokenService.getToken(token);
        if(!mayBeVt.isPresent()){
            LOGGER.info("Trying to validate token {}, but it does not exist", token);
            return VerificationToken.Status.NOT_EXISTS;
        }
        VerificationToken vt = mayBeVt.get();

        if(!vt.isValidToken()){
            LOGGER.info("Trying to validate token {}, but it has expired", token);
            return VerificationToken.Status.EXPIRED;
        }
        userDao.verifyEmail(vt.getUserId());
        login(vt.getUserId());
        verificationTokenService.deleteEmailToken(vt.getUserId());
        return VerificationToken.Status.SUCCESFFULLY_VERIFIED;
    }

    @Override
    @Transactional
    public VerificationToken.Status resendEmailVerification(String email) {

        Optional<User> mayBeUser = userDao.findByEmail(email);
        if(!mayBeUser.isPresent()){
            LOGGER.info("Trying to resend verification email to {}, but this email does not exist", email);
            return VerificationToken.Status.NOT_EXISTS;
        }

        User user = mayBeUser.get();
        if(!user.getStatus().equals(UserStatus.UNABLE)){
            LOGGER.info("Trying to resend verification email to {}, but this email is already registered", email);
            return VerificationToken.Status.ALREADY_VERIFIED;
        }

        verificationTokenService.deleteEmailToken(user.getId());
        final VerificationToken token = verificationTokenService.newToken(user.getId());
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        emailService.sendVerificationEmail(user, token, locale);
        return VerificationToken.Status.SUCCESSFULLY_RESENDED;
    }

    @Override
    @Transactional
    public void addRole(long userId, Role role) {
        userDao.getUserById(userId).orElseThrow(UserNotFoundException::new).addRole(role);
    }

    @Override
    @Transactional
    public void updateProfile(long userId, String username, byte[] bytes, String dataType, String description) {
        User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);
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
    public void followUser(long userId) {
        User myUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        userDao.addFollow(myUser.getId(), userId);
    }

    @Override
    @Transactional
    public void unfollowUser(long userId) {
        User myUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        userDao.unfollow(myUser.getId(), userId);
    }

    @Override
    public boolean isFollowing(long userId) {
        User myUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        return userDao.isFollowing(myUser.getId(), userId);
    }

    /*https://www.baeldung.com/spring-security-auto-login-user-after-registration*/
    private void login(long userId) {
        User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPass(), new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.debug("User {} has loged in automatically", user);
    }

    @Override
    public List<User> getTopCreators(int qty) {
        return userDao.getTopCreators(qty);
    }

    @Override
    public Page<User> searchUsers(int page, String search) {
        return userDao.searchUsers(page, search);
    }


    @Override
    public void pingNewsToggle(News news) {
        Optional<User> maybeUser = securityService.getCurrentUser();
        if (!maybeUser.isPresent() || maybeUser.get().getUserId() != news.getCreatorId()) {
            throw new UserNotAuthorized();
        }
        userDao.pingNewsToggle(maybeUser.get(), news);
    }


    @Override
    public ProfileCategory getProfileCategory(String category, User profile) {
        ProfileCategory cat;
        try {
            cat = ProfileCategory.valueOf(category);
        } catch(IllegalArgumentException e) {
            throw new InvalidFilterException(e);
        }
        if (!profile.getRoles().contains(Role.ROLE_JOURNALIST) && cat.equals(ProfileCategory.MY_POSTS)){
            throw new InvalidFilterException();
        }

        if (cat.equals(ProfileCategory.SAVED) &&
                !(securityService.getCurrentUser().isPresent() &&
                securityService.getCurrentUser().get().equals(profile))){
            throw new InvalidFilterException();
        }

        return cat;
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
    public boolean isUserAdmin(User user) {
        if(user == null)
            return false;
        Collection<Role> roles = user.getRoles();

        return roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_OWNER);
    }
}
