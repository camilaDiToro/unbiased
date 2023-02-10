package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class UserNotAuthorizedExceptionMapper implements ExceptionMapper<UserNotAuthorizedException> {

    private static final String MESSAGE = "User unauthorized";

    @Override
    public Response toResponse(UserNotAuthorizedException e) {
        ApiErrorDto dto = new ApiErrorDto(MESSAGE, ApiErrorCode.UNAUTHORIZED, e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).entity(dto).build();
    }
}
