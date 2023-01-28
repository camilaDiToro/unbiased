package ar.edu.itba.paw.webapp.auth.email;

import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.service.VerificationTokenService;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidEmailTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EmailAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public EmailAuthProvider(UserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authenticationToken = (String) authentication.getCredentials();
        EmailCredentials emailCredentials = getEmailCredentialsFromBasic(authenticationToken);
        final VerificationToken.Status status = userService.verifyUserEmail(emailCredentials.getToken());
        if (!status.equals(VerificationToken.Status.SUCCESFFULLY_VERIFIED)){
            throw new InvalidEmailTokenException(status.getMsg());
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(emailCredentials.getEmail());
        return new EmailAuthToken(userDetails, emailCredentials, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    private EmailCredentials getEmailCredentialsFromBasic(String token){
        // email:token
        byte[] credDecoded = Base64.getDecoder().decode(token);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return new EmailCredentials(values[0], values[1]);
    }
}
