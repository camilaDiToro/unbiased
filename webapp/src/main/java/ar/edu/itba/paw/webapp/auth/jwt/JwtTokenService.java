package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
    String createAccessToken(final UserDetails userDetails);
    String createRefreshToken(final UserDetails userDetails);
    UserDetails validateTokenAndGetDetails(final String token);
    void authenticateFromDetails(final UserDetails userDetails);
}
