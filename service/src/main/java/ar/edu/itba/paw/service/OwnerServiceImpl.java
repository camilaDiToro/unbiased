package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerServiceImpl implements OwnerService{

    private final UserDao userDao;

    @Autowired
    public OwnerServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void makeUserAdmin(User user) {
        userDao.merge(user);
        user.addRole(Role.ROLE_ADMIN);
    }

    @Override
    @Transactional
    public void deleteUserAdmin(User user) {
        userDao.merge(user);
        user.removeAdminRole();
    }
}
