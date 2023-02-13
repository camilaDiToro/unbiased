package ar.edu.itba.paw.webapp.auth.exceptions;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class NotExistentEmailTokenException extends AuthenticationException implements ApiErrorExceptionInt {

    private static final String MSG = "The sent token does not exist";
    private static final ApiErrorCode CODE = ApiErrorCode.NOT_EXISTENT_TOKEN;
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private final String details;

    public NotExistentEmailTokenException(String s, Throwable t) {
        super(MSG, t);
        this.details = s;
    }

    public NotExistentEmailTokenException(String s) {
        super(MSG);
        this.details = s;
    }

    @Override
    public ApiErrorCode getApiCode() {
        return CODE;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return STATUS;
    }

    @Override
    public String getApiErrorMessage() {
        return MSG;
    }

    @Override
    public String getDetails() {
        return details;
    }
}