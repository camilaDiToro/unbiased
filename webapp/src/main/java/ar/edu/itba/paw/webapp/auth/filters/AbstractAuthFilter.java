package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.auth.AuthUtils;
import ar.edu.itba.paw.webapp.auth.Credentials;
import ar.edu.itba.paw.webapp.auth.CustomUserDetailsService;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

public class AbstractAuthFilter extends AbstractAuthenticationProcessingFilter {
        private static final int BASIC_LENGTH = 6;
        private static final int JWT_LENGTH = 7;

        @Override
        protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
            return true;
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
            super.successfulAuthentication(request, response, chain, authResult);
            chain.doFilter(request, response);
        }

        public AbstractAuthFilter() {
            super("/api/");
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException {

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if(authHeader == null){
                /* Nothing to be done */
            }
            else if(authHeader.startsWith("Basic ")){
                final Credentials credentials = AuthUtils.getCredentialsFromBasic(authHeader);
                final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword());
                return getAuthenticationManager().authenticate(authenticationToken);
            }
            else if (authHeader.startsWith("Bearer ")) {
                String authToken = authHeader.substring(JWT_LENGTH);
                //return getAuthenticationManager().authenticate(new JwtAuthenticationToken(authToken));
            }

            throw new InsufficientAuthenticationException("No authorization token provided");
        }
    }