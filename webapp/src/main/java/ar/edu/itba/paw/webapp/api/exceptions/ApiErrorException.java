package ar.edu.itba.paw.webapp.api.exceptions;

import org.springframework.http.HttpStatus;

public class ApiErrorException extends RuntimeException implements ApiErrorExceptionInt{

    private final HttpStatus status;
    private final ApiErrorCode code;
    private final String msg;
    private final String details;

    public ApiErrorException(HttpStatus status, ApiErrorCode code, String msg, String details) {
        super(msg);
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.details = details;
    }
    public ApiErrorException(HttpStatus status, ApiErrorCode code, String msg, String details, Throwable t) {
        super(msg,t);
        this.status = status;
        this.code = code;
        this.msg = msg;
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
    public String getApiErrorMessage() {
        return msg;
    }

    @Override
    public String getDetails() {
        return details;
    }
}
