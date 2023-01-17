package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final long userId;
    private final String pageName;

    public CustomUserDetails(long userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, String pageName) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.pageName = pageName;
    }

    public long getUserId() {
        return userId;
    }

    public String getPageName() {
        return pageName;
    }
}
