package ar.edu.itba.paw.webapp.constraints;

import ar.edu.itba.paw.model.news.Category;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class NotExistingNewsCategoryValidator implements ConstraintValidator<NotExistingNewsCategory, String[]> {


    @Override
    public void initialize(NotExistingNewsCategory constraintAnnotation) {
    }

    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        for (String val : value) {
            if(Arrays.stream(Category.values()).map(Category::getInterCode).noneMatch(s -> s.equals(val)))
                return false;
        }
        return true;
    }
}