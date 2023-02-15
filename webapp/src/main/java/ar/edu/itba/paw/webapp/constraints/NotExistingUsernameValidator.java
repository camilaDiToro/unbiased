package ar.edu.itba.paw.webapp.constraints;

import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NotExistingUsernameValidator implements ConstraintValidator<NotExistingUsername, String> {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    @Override
    public void initialize(NotExistingUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = userService.findByUsername(value);
        return !user.isPresent() || securityService.getCurrentUser().orElseThrow(()->new UserNotAuthorizedException("User should be logged in")).getId() == user.get().getId();
    }
}