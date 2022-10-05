package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.RoleDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    public String getCurrentUserEmail() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null && securityContext.getAuthentication() != null) {
             return securityContext.getAuthentication().getName();
        }
        return null;
    }

    @Override
    public Optional<User> getCurrentUser() {
        String email = getCurrentUserEmail();

        if (email == null){
            return Optional.empty();
        }

        return userDao.findByEmail(email);
    }

    @Override
    public boolean isCurrentUserAdmin() {
        Optional<User> mayBeUser = getCurrentUser();
        if(!mayBeUser.isPresent()){
            return false;
        }
        User user= mayBeUser.get();
        List<String> roles = roleDao.getRoles(user.getId());
        return roles.contains(Role.ADMIN.getRole());
    }
}