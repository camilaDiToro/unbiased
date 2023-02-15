package ar.edu.itba.paw.webapp.api.exceptions;

public class MissingArgumentException extends CustomBadRequestException{

    private static final ApiErrorCode CODE = ApiErrorCode.MISSING_ARGUMENTS;
    private final String details;

    public MissingArgumentException(String details) {
        super(CODE, details);
        this.details = details;
    }
}