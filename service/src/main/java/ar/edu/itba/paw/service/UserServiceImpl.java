package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    ///private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao /*, final PasswordEncoder passwordEncoder*/) {
        this.userDao = userDao;
        //this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User create(User.UserBuilder userBuilder) {
        //return userDao.create(username, passwordEncoder.encode(password));
        return userDao.create(userBuilder);
    }

    @Override
    public User createIfNotExists(User.UserBuilder userBuilder) {
        return userDao.createIfNotExists(userBuilder);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
