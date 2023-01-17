package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.auth.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.MalformedURLException;

public interface JwtTokenService {
    String createAccessToken(final CustomUserDetails userDetails) throws MalformedURLException;
    String createRefreshToken(final CustomUserDetails userDetails) throws MalformedURLException;
    JwtTokenDetails validateTokenAndGetDetails(final String token);
}
