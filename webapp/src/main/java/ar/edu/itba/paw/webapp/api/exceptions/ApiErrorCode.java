package ar.edu.itba.paw.webapp.api.exceptions;

public enum ApiErrorCode {

    //Authentication
    ACCESS_DENIED(600, "Access denied"),
    USER_DISABLED(601, "User disabled"),
    INVALID_JWT_TOKEN(602, "Invalid JWT token"),
    INVALID_JWT_CLAIM(603, "Invalid JWT claim"),
    EXPIRED_JWT_TOKEN(604, "Invalid authentication JWT token"),
    FORBIDDEN(605, "Forbidden"),
    UNAUTHORIZED(606, "Unauthorized"),
    INVALID_EMAIL_TOKEN(607, "Invalid authentication email token"),
    ALREADY_USED_TOKEN(608, "This user is already verified, verification tokens can be used just once"),
    NOT_EXISTENT_TOKEN(609, "The sent token does not exist"),

    //Validation
    INVALID_ROLE(702, "Trying to add an invalid role to an user"),
    INVALID_PARAMETERS(703, "Invalid parameters"),
    INVALID_GET_USERS_FILTER(704, "Invalid get users filter"),
    INVALID_GET_NEWS_FILTER(705, "Invalid get news filter"),
    INVALID_GET_COMMENTS_FILTER(706, "Invalid get comments filter"),
    MISSING_ARGUMENTS(710, "Missing arguments"),

    //Backend
    USER_NOT_FOUND(801, "User not found"),
    NEWS_NOT_FOUND(802, "Article not found"),
    COMMENT_NOT_FOUND(803, "Comment not found"),
    IMAGE_NOT_FOUND(804,  "Image not found"),

    INVALID_CATEGORY(820, "Invalid category"),
    INVALID_FILTER(821, "Invalid filter"),
    INVALID_ORDER(822, "Invalid order"),
    INVALID_TIME_CONSTRAINT(823, "Invalid time constraint"),

    UNKNOWN(900, "Unknown error"),

    // Form validation
    FORM_VALIDATION_ERROR(999, "Unkonwn validation error"),

    USERFORM_EMAIL_REPEATED(1000, "An user with this email already exists"),
    USERFORM_EMAIL_FORMAT(1001, "Invalid email format"),
    USERFORM_PASS_NOTBLAK(1012, "Password can not be blank"),

    USERPROFILEFORM_USERNAME_REPEATED(1100, "The provided username already exists"),
    USERPROFILEFORM_USERNAME_LENGTH(1103, "The username can have up to 50 characters"),
    USERPROFILEFORM_MAILOPTIONS_NOTFOUND(1114, "The provided mail option is invalid"),

    REPORTNEWSFORM_REASON_NOTBLANK(1202, "The report reason can not be blank"),
    REPORTNEWSFORM_REASON_LENGTH(1203,"The report reason can have up to 400 characters"),
    REPORTNEWSFORM_REASON_NOTFOUND(1204,"The provided report reason option is invalid"),
    REPORTNEWSFORM_REASON_NOTNULL(1205,"The reported reason can not be null"),

    CREATENEWSFORM_TITLE_NOTBLANK(1302,"The title can not be blank"),
    CREATENEWSFORM_TITLE_LENGTH(1303,"The title can have up to 200 characters"),
    CREATENEWSFORM_TITLE_NOTNULL(1305,"The title can not be null"),
    CREATENEWSFORM_SUBTITLE_NOTBLANK(1312,"The subtitle can not be blank"),
    CREATENEWSFORM_SUBTITLE_LENGTH(1313,"The subtitle have up to 400 characters"),
    CREATENEWSFORM_SUBTITLE_NOTNULL(1315,"The subtitle can not be null"),
    CREATENEWSFORM_BODY_NOTBLANK(1322,"The body of the article can not be blank"),
    CREATENEWSFORM_BODY_LENGTH(1323,"The body of the article can have up to 10000000 characters"),
    CREATENEWSFORM_BODY_NOTNULL(1325,"The body of the article can not be null"),
    CREATENEWSFORM_CATEGORIES_NOTFOUND(1334,"The provided category option is invalid");


    private final int errorCode;

    public String getErrorMsg() {
        return errorMsg;
    }

    private final String errorMsg;

    ApiErrorCode(final int errorCode, final String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
