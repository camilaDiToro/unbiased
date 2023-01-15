package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException e) {
        ApiErrorDto dto = new ApiErrorDto(e.getMessage(), ApiErrorCode.ACCESS_DENIED, e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(dto).build();
    }
}