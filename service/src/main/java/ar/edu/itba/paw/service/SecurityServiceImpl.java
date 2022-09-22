package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserService userService;

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
}