package ar.edu.itba.paw.webapp.constraints;

import ar.edu.itba.paw.model.user.MailOption;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class NotExistingMailOptionValidator implements ConstraintValidator<NotExistingMailOption, String[]> {


    @Override
    public void initialize(NotExistingMailOption constraintAnnotation) {
    }

    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        for (String val : value) {
            if(Arrays.stream(MailOption.values()).map(MailOption::getInterCode).noneMatch(s -> s.equals(val)))
                return false;
        }
        return true;
    }
}