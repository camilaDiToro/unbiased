package ar.edu.itba.paw.webapp.auth.filters;

import ar.edu.itba.paw.webapp.auth.Credentials;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthToken;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AbstractAuthFilter extends AbstractAuthenticationProcessingFilter {
        private static final int BASIC_LENGTH = 6;
        private static final int JWT_LENGTH = 7;


        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
            super.successfulAuthentication(request, response, chain, authResult);
            chain.doFilter(request, response);
        }

        public AbstractAuthFilter() {
            super(new OrRequestMatcher(
                    new AntPathRequestMatcher("/api/users/{\\d+}", HttpMethod.PUT),
                    new AntPathRequestMatcher("/api/users/{\\d+}/pingNews/{\\d+}", HttpMethod.PUT),
                    // no se que estoy haciendo mal jeje
//                    new RegexRequestMatcher("/api/news/\\d+\\?(.)*savedNews=(.)+", HttpMethod.GET),
//                    new AntPathRequestMatcher("/api/news/{\\d+}/likes/{\\d+}", HttpMethod.PUT),
//                    new AntPathRequestMatcher("/api/news/{\\d+}/dislikes/{\\d+}", HttpMethod.PUT),
//                    new AntPathRequestMatcher("/api/news/{\\d+}/likes/{\\d+}", HttpMethod.DELETE),
//                    new AntPathRequestMatcher("/api/news/{\\d+}/dislikes/{\\d+}", HttpMethod.DELETE),
                    new AntPathRequestMatcher("/api/users/{\\d+}/followers/{\\d+}", HttpMethod.PUT),
                    new AntPathRequestMatcher("/api/users/{\\d+}/followers/{\\d+}", HttpMethod.DELETE),
                    new AntPathRequestMatcher("/api/news/{\\d+}/comments", HttpMethod.POST),
                    new AntPathRequestMatcher("/api/news/{\\d+}/comments/{\\d+}", HttpMethod.DELETE)
            ));
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                throws AuthenticationException {

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if(authHeader == null){
                /* Nothing to be done */
            }
            else if(authHeader.startsWith("Basic ")){
                final Credentials credentials = getCredentialsFromBasic(authHeader);
                final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword());
                return getAuthenticationManager().authenticate(authenticationToken);
            }
            else if (authHeader.startsWith("Bearer ")) {
                final String authToken = authHeader.substring(JWT_LENGTH);
                return getAuthenticationManager().authenticate(new JwtAuthToken(authToken));
            }

            throw new InsufficientAuthenticationException("No authorization token provided");
        }

    // https://stackoverflow.com/questions/16000517/how-to-get-password-from-http-basic-authentication
    private Credentials getCredentialsFromBasic(String basic){
        // Authorization:Basic email:password
        String base64Credentials = basic.substring(BASIC_LENGTH).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return new Credentials(values[0], values[1]);
    }
}