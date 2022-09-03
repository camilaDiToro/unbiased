package ar.edu.itba.paw.webapp.constraints;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileType, CommonsMultipartFile> {


    @Override
    public void initialize(FileType fileType) {}

    @Override
    public boolean isValid(CommonsMultipartFile commonsMultipartFile, ConstraintValidatorContext constraintValidatorContext) {
        return commonsMultipartFile != null && !commonsMultipartFile.isEmpty();
    }
}
