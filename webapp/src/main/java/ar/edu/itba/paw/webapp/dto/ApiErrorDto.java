package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;

import javax.validation.ConstraintViolation;
import java.util.HashMap;

public class ApiErrorDto {

    private String message;
    private Integer apiCode;
    private String details;

    public ApiErrorDto(ApiErrorCode apiCode, String details) {
        this.message = apiCode.getErrorMsg();
        this.apiCode = apiCode.getErrorCode();
        this.details = details;
    }

    public ApiErrorDto() {
    }


    public static ApiErrorDto fromApiErrorException(final ApiErrorExceptionInt apiErrorException){
        return new ApiErrorDto(
                apiErrorException.getApiCode(),
                apiErrorException.getDetails()
        );
    }

    public static ApiErrorDto fromConstraintViolation(final ConstraintViolation constraintViolation){
        //static HashMap<String, ApiErrorDto>
        ApiErrorDto aed = new ApiErrorDto();



        return aed;
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
