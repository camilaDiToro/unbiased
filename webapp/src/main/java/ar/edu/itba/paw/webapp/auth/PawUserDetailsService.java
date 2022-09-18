package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserStatus;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class PawUserDetailsService implements UserDetailsService {

    private UserService us;

    @Autowired
    public PawUserDetailsService(UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final User user = us.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user '" + username + "'"));

        if (user == null) {
            throw new UsernameNotFoundException("No user by the name " + username);
        }
        final Collection<? extends GrantedAuthority> authorities = us.getRoles(user.getId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                //Arrays.asList(
                //new SimpleGrantedAuthority("ROLE_USER")
                //new SimpleGrantedAuthority("ROLE_ADMIN")
        //);

        return new org.springframework.security.core.userdetails.User(username, user.getPass(), user.getStatus().getStatus().equals(UserStatus.REGISTERED.getStatus()), true, true, true, authorities);
    }
}