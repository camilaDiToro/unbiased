package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectURL = "/login?error=true";
        Optional<User> mayBeUser = userService.findByEmail(request.getParameter("username"));
        if(mayBeUser.isPresent() && !mayBeUser.get().getStatus().getStatus().equals(UserStatus.REGISTERED.getStatus())) {
            redirectURL = "/login?unable=true";
            userService.resendEmailVerification(request.getParameter("username"));
        }
        super.setDefaultFailureUrl(redirectURL);
        super.onAuthenticationFailure(request, response, exception);
    }
}