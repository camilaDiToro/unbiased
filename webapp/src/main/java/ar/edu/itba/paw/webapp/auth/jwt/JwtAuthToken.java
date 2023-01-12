package ar.edu.itba.paw.webapp.auth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class JwtAuthToken extends AbstractAuthenticationToken {

    private String jwtAuthToken;
    private UserDetails userDetails;
    private JwtTokenDetails jwtTokenDetails;


    public JwtAuthToken(String jwtAuthToken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.jwtAuthToken = jwtAuthToken;
        this.setAuthenticated(false);
    }

    public JwtAuthToken(UserDetails userDetails, JwtTokenDetails jwtTokenDetails,
                        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userDetails = userDetails;
        this.jwtTokenDetails = jwtTokenDetails;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot trust this token. Use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return jwtAuthToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public Object getDetails() {
        return jwtTokenDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.jwtAuthToken = null;
    }
}