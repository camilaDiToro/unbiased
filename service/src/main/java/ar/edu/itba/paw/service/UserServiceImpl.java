package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.VerificationToken;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    private final RoleDao roleDao;
    private final ImageService imageService;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, EmailService emailService, VerificationTokenService verificationTokenService, RoleDao roleDao, ImageService imageService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.roleDao = roleDao;
        this.imageService = imageService;
    }


    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
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
    public User createIfNotExists(User.UserBuilder userBuilder) {
        return userDao.createIfNotExists(userBuilder);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public VerificationToken.Status verifyUserEmail(String token) {
        Optional<VerificationToken> mayBeBt = verificationTokenService.getToken(token);
        if(!mayBeBt.isPresent()){
            return VerificationToken.Status.NOT_EXISTS;
        }
        VerificationToken vt = mayBeBt.get();
        if(!vt.isValidToken()){
            return VerificationToken.Status.EXPIRED;
        }
        userDao.verifyEmail(vt.getUserId());
        login(vt.getUserId());
        verificationTokenService.deleteEmailToken(vt.getUserId());
        return VerificationToken.Status.SUCCESFFULLY_VERIFIED;
    }

    @Override
    public VerificationToken.Status resendEmailVerification(String email) {
        Optional<User> mayBeUser = userDao.findByEmail(email);
        if(!mayBeUser.isPresent())
            return VerificationToken.Status.NOT_EXISTS;
        User user = mayBeUser.get();
        verificationTokenService.deleteEmailToken(user.getId());
        final VerificationToken token = verificationTokenService.newToken(user.getId());
        Locale locale = LocaleContextHolder.getLocale();
        LocaleContextHolder.setLocale(locale, true);
        emailService.sendVerificationEmail(user, token, locale);
        return VerificationToken.Status.SUCCESSFULLY_RESENDED;
    }

    @Override
    public void addRole(long userId, Role role) {
        roleDao.addRole(userId, role);
    }

    @Override
    public List<String> getRoles(long userId) {
        return roleDao.getRoles(userId);
    }

    @Override
    public void updateProfile(long userId, String username, Long imageId) {
        User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);

        if(imageId!=null){
            if(user.getImageId()!=null)
                imageService.deleteImage(user.getImageId());
            userDao.updateImage(userId,imageId);
        }

        if(username!= null && !username.isEmpty())
            userDao.updateUsername(userId,username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /*https://www.baeldung.com/spring-security-auto-login-user-after-registration*/
    private void login(long userId) {
        final User user = getUserById(userId).get();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPass(), new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
