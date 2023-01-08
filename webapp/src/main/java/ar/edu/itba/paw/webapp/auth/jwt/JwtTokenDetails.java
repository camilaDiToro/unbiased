package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtTokenDetails {

    private final UserDetails userDetails;
    private final User user;
    private String id;
    private String username;

    public JwtTokenDetails(UserDetails userDetails, User user) {
        this.userDetails = userDetails;
        this.user = user;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public User getUser() {
        return user;
    }
}
