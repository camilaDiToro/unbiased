package ar.edu.itba.paw.webapp.api.exceptions;

import org.springframework.http.HttpStatus;

public interface ApiErrorException{
    ApiErrorCode getApiCode();
    HttpStatus getHttpStatus();
    String getApiErrorMessage();
    String getDetails();
}
