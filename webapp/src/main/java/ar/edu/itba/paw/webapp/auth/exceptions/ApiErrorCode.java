package ar.edu.itba.paw.webapp.auth.exceptions;

public enum ApiErrorCode {

    //Authentication
    ACCESS_DENIED(600),
    USER_DISABLED(601),
    INVALID_JWT_TOKEN(602),
    INVALID_JWT_CLAIM(603),
    EXPIRED_JWT_TOKEN(604),
    FORBIDDEN(605),
    UNAUTHORIZED(606);

    private final int errorCode;

    ApiErrorCode(final int errorCode){
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
