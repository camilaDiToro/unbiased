package ar.edu.itba.paw.webapp.api.exceptions;

import org.springframework.http.HttpStatus;

public class CustomBadRequestException extends ApiErrorException{

    private static final HttpStatus status = HttpStatus.BAD_REQUEST;
    private final ApiErrorCode code;
    private final String details;

    public CustomBadRequestException(ApiErrorCode code, String details) {
        super(status,code,details);
        this.code = code;
        this.details = details;
    }
}
