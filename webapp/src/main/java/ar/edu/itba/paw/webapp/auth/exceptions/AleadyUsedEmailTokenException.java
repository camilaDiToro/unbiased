package ar.edu.itba.paw.webapp.auth.exceptions;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class AleadyUsedEmailTokenException extends AuthenticationException implements ApiErrorExceptionInt {

    private static final String MSG = "This user is already verified, verification tokens can be used just once";
    private static final ApiErrorCode CODE = ApiErrorCode.ALREADY_USED_TOKEN;
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;
    private final String details;

    public AleadyUsedEmailTokenException(String s, Throwable t) {
        super(MSG, t);
        this.details = s;
    }

    public AleadyUsedEmailTokenException(String s) {
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
