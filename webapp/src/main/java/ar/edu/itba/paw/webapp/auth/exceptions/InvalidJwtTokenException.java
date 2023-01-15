package ar.edu.itba.paw.webapp.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import javax.ws.rs.core.Response;

public class InvalidJwtTokenException extends AuthenticationException implements ApiErrorException {

    private static final String MSG = "Invalid JWT token: %s";
    private static final ApiErrorCode CODE = ApiErrorCode.INVALID_JWT_CLAIM;
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private final String details;

    public InvalidJwtTokenException(String s, Throwable t) {
        super(String.format(MSG, s), t);
        this.details = s;
    }

    public InvalidJwtTokenException(String s) {
        super(String.format(MSG, s));
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
        return String.format(MSG, details);
    }

    @Override
    public String getDetails() {
        return details;
    }
}
