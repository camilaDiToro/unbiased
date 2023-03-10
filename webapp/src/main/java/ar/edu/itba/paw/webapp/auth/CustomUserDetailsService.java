package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.user.Tier;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService us;

    @Autowired
    public CustomUserDetailsService(UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final User user = us.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user '" + username + "'"));

        if (user == null) {
            throw new UsernameNotFoundException("No user by the name " + username);
        }
        final Collection<? extends GrantedAuthority> authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());

        return new CustomUserDetails(user.getId(), username, user.getPass(), user.getStatus().getStatus().equals(UserStatus.REGISTERED.getStatus()), true, true, true, authorities, user.toString(), Tier.getTier(us.getFollowersCount(user.getUserId())).toString(), user.hasImage());
    }
}