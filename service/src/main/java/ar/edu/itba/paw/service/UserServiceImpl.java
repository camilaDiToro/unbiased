package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.InvalidFilterException;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.persistence.RoleDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    private final RoleDao roleDao;
    private final ImageService imageService;
    private final SecurityService securityService;


    @Autowired

    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, EmailService emailService, VerificationTokenService verificationTokenService, RoleDao roleDao, ImageService imageService, SecurityService securityService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.roleDao = roleDao;
        this.imageService = imageService;
        this.securityService = securityService;
    }


    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public Optional<User> getRegisteredUserById(long id) {
        Optional<User> mayBeUser = userDao.getUserById(id);
        if(!mayBeUser.isPresent())
            return Optional.empty();
        if(!mayBeUser.get().getStatus().getStatus().equals(UserStatus.REGISTERED.getStatus()))
            return Optional.empty();
        return mayBeUser;
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
        Optional<VerificationToken> mayBeBt = verificationTokenService.getToken(token);
        if(!mayBeBt.isPresent()){
            return VerificationToken.Status.NOT_EXISTS;
        }
        VerificationToken vt = mayBeBt.get();
        User user = userDao.getUserById(vt.getUserId()).get();

        if(!vt.isValidToken()){
            return VerificationToken.Status.EXPIRED;
        }
        userDao.verifyEmail(vt.getUserId());
        login(getUserById(vt.getUserId()).orElseThrow(UserNotFoundException::new));
        verificationTokenService.deleteEmailToken(user);
        return VerificationToken.Status.SUCCESFFULLY_VERIFIED;
    }

    @Override
    @Transactional
    public VerificationToken.Status resendEmailVerification(String email) {
        Optional<User> mayBeUser = userDao.findByEmail(email);
        if(!mayBeUser.isPresent())
            return VerificationToken.Status.NOT_EXISTS;
        User user = mayBeUser.get();
        if(user.getStatus().equals(UserStatus.REGISTERED))
            return VerificationToken.Status.ALREADY_VERIFIED;
        verificationTokenService.deleteEmailToken(user);
        final VerificationToken token = verificationTokenService.newToken(user.getId());
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        emailService.sendVerificationEmail(user, token, locale);
        return VerificationToken.Status.SUCCESSFULLY_RESENDED;
    }

    @Override
    @Transactional
    public void addRole(User user, Role role) {
        roleDao.addRole(user.getId(), role);
    }

    @Override
    public List<Role> getRoles(User user) {
        return roleDao.getRoles(user.getId()).stream().map(Role::getRole).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateProfile(User user, String username, Long imageId) {
        long id = user.getId();
        if(imageId!=null){
            if(user.getImageId()!=null)
                imageService.deleteImage(user.getImageId());
            userDao.updateImage(user,imageId);
        }

        if(username!= null && !username.isEmpty())
            userDao.updateUsername(user,username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public void followUser(User user) {
        User myUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        userDao.addFollow(myUser.getId(), user.getId());
    }

    @Override
    @Transactional
    public void unfollowUser(User user) {
        User myUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        userDao.unfollow(myUser.getId(), user.getId());
    }

    @Override
    public boolean isFollowing(User user) {
        User myUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        return userDao.isFollowing(myUser.getId(), user.getId());
    }

    /*https://www.baeldung.com/spring-security-auto-login-user-after-registration*/
    private void login(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPass(), new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
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
    public ProfileCategory getProfileCategory(String category, User profile) {
        ProfileCategory cat;
        try {
            cat = ProfileCategory.valueOf(category);
        } catch(IllegalArgumentException e) {
            throw new InvalidFilterException();
        }
        if (!getRoles(profile).contains(Role.JOURNALIST) && cat.equals(ProfileCategory.MY_POSTS))
            throw new InvalidFilterException();

        if (cat.equals(ProfileCategory.SAVED) &&
                !(securityService.getCurrentUser().isPresent() &&
                securityService.getCurrentUser().get().equals(profile)))
            throw new InvalidFilterException();

        return cat;
    }
}
