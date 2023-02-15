package ar.edu.itba.paw.webapp.auth.exceptions;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class ExpiredJwtTokenException extends AuthenticationException implements ApiErrorExceptionInt {

    private static final ApiErrorCode CODE = ApiErrorCode.EXPIRED_JWT_TOKEN;
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private final String details;

    public ExpiredJwtTokenException(String s, Throwable t) {
        super(CODE.getErrorMsg() + ": " + s, t);
        this.details = s;
    }

    public ExpiredJwtTokenException(String s) {
        super(CODE.getErrorMsg() + ": " + s);
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
    public String getDetails() {
        return details;
    }

}
