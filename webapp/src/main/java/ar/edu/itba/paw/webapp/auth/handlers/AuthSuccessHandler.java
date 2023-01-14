package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthToken;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenDetails;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenService;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if((!(authentication instanceof JwtAuthToken)) || ((JwtTokenDetails) authentication.getDetails()).getTokenType().equals(JwtTokenType.REFRESH)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            response.addHeader("access-token", "Bearer " + jwtTokenService.createAccessToken(userDetails));
            response.addHeader("refresh-token", "Bearer " + jwtTokenService.createRefreshToken(userDetails));
        }
    }
}