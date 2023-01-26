package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;

public class ApiErrorDto {

    private String message;
    private Integer apiCode;
    private String details;

    public ApiErrorDto(String message, ApiErrorCode apiCode, String details) {
        this.message = message;
        this.apiCode = apiCode.getErrorCode();
        this.details = details;
    }

    public ApiErrorDto() {
    }


    public static ApiErrorDto fromApiErrorException(final ApiErrorExceptionInt apiErrorException){
        return new ApiErrorDto(
                apiErrorException.getApiErrorMessage(),
                apiErrorException.getApiCode(),
                apiErrorException.getDetails()
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
