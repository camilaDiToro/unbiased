package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class OwnerServiceImpl implements OwnerService{

    private final UserDao userDao;
    private final EmailService emailService;

    @Autowired
    public OwnerServiceImpl(final UserDao userDao,final EmailService emailService) {
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void makeUserAdmin(String email) {
        final User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(!user.getRoles().contains(Role.ROLE_ADMIN)){
            user.addRole(Role.ROLE_ADMIN);
            final Locale locale = user.getEmailSettings() != null ? user.getEmailSettings().getLocale() : LocaleContextHolder.getLocale();
            emailService.sendAdminEmail(user, locale);
        }
    }

    @Override
    @Transactional
    public void deleteUserAdmin(long userId) {
        final User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);
        user.removeAdminRole();
    }

    @Override
    public Page<User> getAdmins(int page, String search) {
        return userDao.getAdmins(page,search);
    }
}
