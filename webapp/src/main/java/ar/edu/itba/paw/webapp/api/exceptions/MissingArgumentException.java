package ar.edu.itba.paw.webapp.api.exceptions;

public class MissingArgumentException extends CustomBadRequestException{

    private static final ApiErrorCode CODE = ApiErrorCode.MISSING_ARGUMENTS;
    private static final String MSG = "Missing arguments";
    private final String details;

    public MissingArgumentException(String details) {
        super(CODE, MSG, details);
        this.details = details;
    }
}