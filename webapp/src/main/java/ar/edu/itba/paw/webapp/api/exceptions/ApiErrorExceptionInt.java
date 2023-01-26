package ar.edu.itba.paw.webapp.api.exceptions;

import org.springframework.http.HttpStatus;

public interface ApiErrorExceptionInt {
    ApiErrorCode getApiCode();
    HttpStatus getHttpStatus();
    String getApiErrorMessage();
    String getDetails();
}
