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
    INVALID_EMAIL_TOKEN(607),

    //Validation
    VALIDATION(701),
    INVALID_ROLE(702),
    INVALID_PARAMETERS(703),
    INVALID_GET_USERS_FILTER(704),
    INVALID_GET_NEWS_FILTER(705),
    MISSING_ARGUMENTS(706),


    //Backend
    USER_NOT_FOUND(801),
    NEWS_NOT_FOUND(802),
    COMMENT_NOT_FOUND(803),
    IMAGE_NOT_FOUND(804),

    INVALID_CATEGORY(820),
    INVALID_FILTER(821),
    INVALID_ORDER(822),
    INVALID_TIME_CONSTRAINT(823),

    UNKNOWN(900),
    WEB_APP_ERROR(901);


    private final int errorCode;

    ApiErrorCode(final int errorCode){
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
