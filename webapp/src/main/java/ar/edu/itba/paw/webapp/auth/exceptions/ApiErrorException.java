package ar.edu.itba.paw.webapp.auth.exceptions;

import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;

public interface ApiErrorException{
    ApiErrorCode getApiCode();
    HttpStatus getHttpStatus();
    String getApiErrorMessage();
    String getDetails();
}
