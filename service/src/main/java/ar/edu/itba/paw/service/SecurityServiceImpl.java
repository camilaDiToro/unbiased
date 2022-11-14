package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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


    @Override
    public Optional<String> getCurrentUserEmail() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null && securityContext.getAuthentication() != null) {
             return Optional.of(securityContext.getAuthentication().getName());
        }
        return Optional.empty();
    }
    @Cacheable
    @Override
    public Optional<User> getCurrentUser() {
        final Optional<String> mayBeEmail = getCurrentUserEmail();

        if (!mayBeEmail.isPresent()){
            return Optional.empty();
        }

        return userDao.findByEmail(mayBeEmail.get());
    }

    @Override
    public boolean isCurrentUserAdmin() {
        final Optional<User> mayBeUser = getCurrentUser();
        if(!mayBeUser.isPresent()){
            return false;
        }

        final Collection<Role> roles = mayBeUser.get().getRoles();
        return roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_OWNER);
    }
}