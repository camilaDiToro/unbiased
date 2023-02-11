package ar.edu.itba.paw.webapp.auth.email;

import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.service.VerificationTokenService;
import ar.edu.itba.paw.webapp.auth.exceptions.AleadyUsedEmailTokenException;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidEmailTokenException;
import ar.edu.itba.paw.webapp.auth.exceptions.NotExistentEmailTokenException;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
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
            if(userService.isUserEnabled(emailCredentials.getEmail())){
                throw new AleadyUsedEmailTokenException(emailCredentials.getEmail());
            }
            throw new NotExistentEmailTokenException(emailCredentials.getToken());
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(emailCredentials.getEmail());
        return new EmailAuthToken(userDetails, emailCredentials, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (EmailAuthToken.class.isAssignableFrom(aClass));
    }

    private EmailCredentials getEmailCredentialsFromBasic(String token){
        // email:token
        try{
            byte[] credDecoded = Base64.getDecoder().decode(token);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            return new EmailCredentials(values[0], values[1]);
        }catch (Exception e){
            throw new InvalidEmailTokenException(e.getMessage(), e);
        }
    }
}
