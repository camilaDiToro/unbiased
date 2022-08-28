package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnotherUserServiceImpl implements UserService {

   private final UserDao userDao;

    @Autowired
    public AnotherUserServiceImpl(final UserDao userDao) {
            this.userDao = userDao;
        }


        @Override
        public Optional<User> getUserById(long id) {
            return userDao.getUserById(id);
        }

        @Override
        public User create(String email) {
            return userDao.create(email);
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return userDao.findByEmail(email);
        }
}
