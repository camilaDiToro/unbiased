package ar.edu.itba.paw.webapp.auth.email;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class EmailAuthToken extends AbstractAuthenticationToken {

    private String emailToken;
    private UserDetails userDetails;
    private EmailCredentials emailCredentials;

    public EmailAuthToken(final String emailToken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.emailToken = emailToken;
        this.setAuthenticated(false);
    }

    public EmailAuthToken(final UserDetails userDetails, final EmailCredentials emailCredentials,
                          final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.emailCredentials = emailCredentials;
        this.userDetails = userDetails;
        this.emailToken = emailCredentials.getToken();
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return emailToken;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        emailToken = null;
    }
}
