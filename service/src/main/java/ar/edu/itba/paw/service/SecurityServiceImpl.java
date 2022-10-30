package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserDao userDao;

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

        Collection<Role> roles = mayBeUser.get().getRoles();

        return roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_OWNER);
    }
}