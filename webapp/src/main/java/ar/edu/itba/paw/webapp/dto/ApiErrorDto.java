package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;

import static ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode.*;

public class ApiErrorDto {

    private String message;
    private Integer apiCode;
    private String details;

    static Map<String, ApiErrorCode> errorMapper = new HashMap<>();
    static{
        errorMapper.put("userform.email.repeated", USERFORM_EMAIL_REPEATED);
        errorMapper.put("userform.email.invalid", USERFORM_EMAIL_FORMAT);
        errorMapper.put("userform.pass.notblank", USERFORM_PASS_NOTBLAK);
        errorMapper.put("userprofileform.username.repeated", USERPROFILEFORM_USERNAME_REPEATED);
        errorMapper.put("userprofileform.username.length", USERPROFILEFORM_USERNAME_LENGTH);
        errorMapper.put("userprofileform.mailoptions.notfound", USERPROFILEFORM_MAILOPTIONS_NOTFOUND);

        errorMapper.put("reportnewsform.reason.notblank", REPORTNEWSFORM_REASON_NOTBLANK);
        errorMapper.put("reportnewsform.reason.length", REPORTNEWSFORM_REASON_LENGTH);
        errorMapper.put("reportnewsform.reason.notfound", REPORTNEWSFORM_REASON_NOTFOUND);
        errorMapper.put("reportnewsform.reason.notnull", REPORTNEWSFORM_REASON_NOTNULL);

        errorMapper.put("createnewsform.title.notblank", CREATENEWSFORM_TITLE_NOTBLANK);
        errorMapper.put("createnewsform.title.length", CREATENEWSFORM_TITLE_LENGTH);
        errorMapper.put("createnewsform.title.notnull", CREATENEWSFORM_TITLE_NOTNULL);
        errorMapper.put("createnewsform.subtitle.notblank", CREATENEWSFORM_SUBTITLE_NOTBLANK);
        errorMapper.put("createnewsform.subtitle.length", CREATENEWSFORM_SUBTITLE_LENGTH);
        errorMapper.put("createnewsform.subtitle.notnull", CREATENEWSFORM_SUBTITLE_NOTNULL);
        errorMapper.put("createnewsform.body.notblank", CREATENEWSFORM_BODY_NOTBLANK);
        errorMapper.put("createnewsform.body.length", CREATENEWSFORM_BODY_LENGTH);
        errorMapper.put("createnewsform.body.notnull", CREATENEWSFORM_BODY_NOTNULL);
        errorMapper.put("createnewsform.categories.notfound", CREATENEWSFORM_CATEGORIES_NOTFOUND);

        errorMapper.put("commentnewsform.comment.notblank", COMMENTNEWSFORM_COMMENT_NOTBLANK);
        errorMapper.put("commentnewsform.comment.length", COMMENTNEWSFORM_COMMENT_LENGTH);
        errorMapper.put("commentnewsform.comment.notnull", COMMENTNEWSFORMM_COMMENT_NOTNULL);
    }

    public ApiErrorDto(ApiErrorCode apiCode, String details) {
        this.message = apiCode.getErrorMsg();
        this.apiCode = apiCode.getErrorCode();
        this.details = details;
    }

    public ApiErrorDto() {
    }


    public static ApiErrorDto fromApiErrorException(final ApiErrorExceptionInt apiErrorException) {
        return new ApiErrorDto(
                apiErrorException.getApiCode(),
                apiErrorException.getDetails()
        );
    }

    public static ApiErrorDto fromConstraintViolation(ConstraintViolation constraintViolation){
        return new ApiErrorDto(
                errorMapper.getOrDefault(constraintViolation.getMessage(), FORM_VALIDATION_ERROR),
                constraintViolation.getMessage()
        );
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getApiCode() {
        return apiCode;
    }

    public void setApiCode(Integer apiCode) {
        this.apiCode = apiCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
