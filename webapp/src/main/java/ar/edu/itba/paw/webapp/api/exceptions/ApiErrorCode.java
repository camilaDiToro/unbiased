package ar.edu.itba.paw.webapp.api.exceptions;

public enum ApiErrorCode {

    //Authentication
    ACCESS_DENIED(600),
    USER_DISABLED(601),
    INVALID_JWT_TOKEN(602),
    INVALID_JWT_CLAIM(603),
    EXPIRED_JWT_TOKEN(604),
    FORBIDDEN(605),
    UNAUTHORIZED(606),

    //Validation
    VALIDATION(701),

    //Backend
    USER_NOT_FOUND(801),
    NEWS_NOT_FOUND(802);

    private final int errorCode;

    ApiErrorCode(final int errorCode){
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}