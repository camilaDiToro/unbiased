package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final long userId;
    private final String pageName;

    public String getTier() {
        return tier;
    }

    private final String tier;

    public boolean hasImage() {
        return hasImage;
    }

    private final boolean hasImage;

    public CustomUserDetails(long userId, String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                             String pageName, String tier, boolean hasImage) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.pageName = pageName;
        this.tier = tier;
        this.hasImage = hasImage;
    }

    public long getUserId() {
        return userId;
    }

    public String getPageName() {
        return pageName;
    }
}
