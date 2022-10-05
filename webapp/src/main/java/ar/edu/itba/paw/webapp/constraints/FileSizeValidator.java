package ar.edu.itba.paw.webapp.constraints;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private static final long mb = 10;

    @Override
    public void initialize(FileSize constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.getSize() == 0) {
            return true;
        }
        try {
            value.getBytes();
        } catch (IOException e) {
            return false;
        }
        return value.getSize() <= mb * 1024*1024;
    }
}