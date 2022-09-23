package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    public String getCurrentUserEmail() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null) return authentication.getName();
        }
        return null;
    }

    @Override
    public Optional<User> getCurrentUser() {
        String username = getCurrentUserEmail();

        if (username == null) return Optional.empty();

        return userService.findByEmail(getCurrentUserEmail());
    }

    @Override
    public boolean isCurrentUserAdmin() {
        Optional<User> mayBeUser = getCurrentUser();
        if(!mayBeUser.isPresent())
            return false;
        User user= mayBeUser.get();
        List<String> roles = roleDao.getRoles(user.getId());
        return roles.contains(Role.ADMIN);
    }
}