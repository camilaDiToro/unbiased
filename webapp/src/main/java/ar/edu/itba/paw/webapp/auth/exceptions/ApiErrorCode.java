package ar.edu.itba.paw.webapp.auth.exceptions;

public enum ApiErrorCode {

    ACCESS_DENIED(600),
    USER_DISABLED(601);

    private final int errorCode;

    ApiErrorCode(final int errorCode){
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
