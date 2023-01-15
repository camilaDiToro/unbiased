package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorException;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;

public class ApiErrorDto {

    private String message;
    private Integer apiCode;
    private Integer status;
    private String details;

    public ApiErrorDto(String message, ApiErrorCode apiCode, String details, HttpStatus status) {
        this.message = message;
        this.apiCode = apiCode.getErrorCode();
        this.details = details;
        this.status = status.value();
    }


    public static ApiErrorDto fromApiErrorException(final ApiErrorException apiErrorException){
        return new ApiErrorDto(
                apiErrorException.getApiErrorMessage(),
                apiErrorException.getApiCode(),
                apiErrorException.getDetails(),
                apiErrorException.getHttpStatus()
        );
    }

    public static ApiErrorDto fromValidationException(final ConstraintViolation<?> violation){
        return new ApiErrorDto(
                "Validation error",
                ApiErrorCode.VALIDATION,
                violation.getMessage(),
                HttpStatus.BAD_REQUEST
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
