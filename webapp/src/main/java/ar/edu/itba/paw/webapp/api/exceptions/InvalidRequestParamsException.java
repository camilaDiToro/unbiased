package ar.edu.itba.paw.webapp.api.exceptions;

public class InvalidRequestParamsException extends CustomBadRequestException{

    private static final ApiErrorCode CODE = ApiErrorCode.INVALID_PARAMETERS;
    private final String details;

    public InvalidRequestParamsException(String details) {
        super(CODE, details);
        this.details = details;
    }
}
