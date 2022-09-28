package ar.edu.itba.paw.webapp.constraints;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistingEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = userService.findByEmail(value);
        return user.isPresent() ;
    }
}