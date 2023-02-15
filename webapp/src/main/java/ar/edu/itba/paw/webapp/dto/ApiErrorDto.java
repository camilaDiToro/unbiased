package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;

import static ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode.*;

public class ApiErrorDto {

    private String message;
    private Integer apiCode;
    private String details;

    static Map<String, ApiErrorCode> errorMapper = new HashMap<>();
    static{
        errorMapper.put("userform.email.repeated", USERFORM_EMAIL_REPEATED);
        errorMapper.put("userform.email.invalid", USERFORM_EMAIL_FORMAT);
        errorMapper.put("userform.pass.not.blank", USERFORM_PASS_NOTBLAK);
        errorMapper.put("userprofileform.username.repeated", USERPROFILEFORM_USERNAME_REPEATED);
        errorMapper.put("userprofileform.username.length", USERPROFILEFORM_USERNAME_LENGTH);
        errorMapper.put("userprofileform.mailoptions.notfound", USERPROFILEFORM_MAILOPTIONS_NOTFOUND);
    }

    public ApiErrorDto(ApiErrorCode apiCode, String details) {
        this.message = apiCode.getErrorMsg();
        this.apiCode = apiCode.getErrorCode();
        this.details = details;
    }

    public ApiErrorDto() {
    }


    public static ApiErrorDto fromApiErrorException(final ApiErrorExceptionInt apiErrorException) {
        return new ApiErrorDto(
                apiErrorException.getApiCode(),
                apiErrorException.getDetails()
        );
    }

    public static ApiErrorDto fromConstraintViolation(ConstraintViolation constraintViolation){
        return new ApiErrorDto(
                errorMapper.getOrDefault(constraintViolation.getMessage(), FORM_VALIDATION_ERROR),
                constraintViolation.getMessage()
        );
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getApiCode() {
        return apiCode;
    }

    public void setApiCode(Integer apiCode) {
        this.apiCode = apiCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
