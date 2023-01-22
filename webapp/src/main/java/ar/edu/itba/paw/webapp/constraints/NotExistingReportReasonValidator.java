package ar.edu.itba.paw.webapp.constraints;

import ar.edu.itba.paw.model.admin.ReportReason;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class NotExistingReportReasonValidator implements ConstraintValidator<NotExistingReportReason, String> {


    @Override
    public void initialize(NotExistingReportReason constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(ReportReason.values()).map(ReportReason::getInterCode).anyMatch(s -> s.equals(value));
    }
}