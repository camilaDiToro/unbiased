package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.auth.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.auth.exceptions.ApiErrorException;

public class ApiErrorDto {

    private String message;
    private ApiErrorCode apiCode;

    public ApiErrorDto(String message, ApiErrorCode apiCode) {
        this.message = message;
        this.apiCode = apiCode;
    }

    public ApiErrorDto() {
    }

    public static ApiErrorDto fromApiErrorException(final ApiErrorException apiErrorException){
        final ApiErrorDto dto = new ApiErrorDto();
        dto.message = apiErrorException.getMessage();
        dto.apiCode = apiErrorException.getApiCode();
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiErrorCode getApiCode() {
        return apiCode;
    }

    public void setApiCode(ApiErrorCode apiCode) {
        this.apiCode = apiCode;
    }
}
