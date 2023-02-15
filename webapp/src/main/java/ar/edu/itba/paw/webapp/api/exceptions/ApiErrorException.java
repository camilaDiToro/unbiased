package ar.edu.itba.paw.webapp.api.exceptions;

import org.springframework.http.HttpStatus;

public class ApiErrorException extends RuntimeException implements ApiErrorExceptionInt{

    private final HttpStatus status;
    private final ApiErrorCode code;
    private final String details;

    public ApiErrorException(HttpStatus status, ApiErrorCode code, String details) {
        super(code.getErrorMsg());
        this.status = status;
        this.code = code;
        this.details = details;
    }
    public ApiErrorException(HttpStatus status, ApiErrorCode code, String details, Throwable t) {
        super(code.getErrorMsg(),t);
        this.status = status;
        this.code = code;
        this.details = details;
    }

    @Override
    public ApiErrorCode getApiCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }


    @Override
    public String getDetails() {
        return details;
    }
}
