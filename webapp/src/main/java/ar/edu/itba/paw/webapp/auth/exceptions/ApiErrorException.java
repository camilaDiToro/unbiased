package ar.edu.itba.paw.webapp.auth.exceptions;

import javax.ws.rs.core.Response;

public class ApiErrorException extends RuntimeException{

    private final ApiErrorCode apiCode;
    private final Response.Status httpStatus;
    private final String message;

    public ApiErrorException(ApiErrorCode apiCode, Response.Status httpStatus, String message) {
        this.apiCode = apiCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ApiErrorException(Throwable var1, ApiErrorCode apiCode, Response.Status httpStatus, String message) {
        super(var1);
        throw new ApiErrorException(apiCode, httpStatus, message);
    }

    public ApiErrorCode getApiCode() {
        return apiCode;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
