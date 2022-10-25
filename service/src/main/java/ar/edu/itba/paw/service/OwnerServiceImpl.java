package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
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
    public OwnerServiceImpl(UserDao userDao, EmailService emailService) {
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void makeUserAdmin(User user) {
        userDao.merge(user);
        if(!user.getRoles().contains(Role.ROLE_ADMIN)){
            user.addRole(Role.ROLE_ADMIN);
            Locale locale = LocaleContextHolder.getLocale();
            LocaleContextHolder.setLocale(locale, true);
            emailService.sendAdminEmail(user, locale);
        }
    }

    @Override
    @Transactional
    public void deleteUserAdmin(User user) {
        userDao.merge(user);
        user.removeAdminRole();
    }

    @Override
    public Page<User> getAdmins(int page, String search) {
        return userDao.getAdmins(page,search);
    }
}
