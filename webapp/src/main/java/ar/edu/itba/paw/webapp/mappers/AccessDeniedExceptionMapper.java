package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.auth.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException e) {
        ApiErrorDto dto = new ApiErrorDto(e.getMessage(), ApiErrorCode.ACCESS_DENIED, e.getMessage(), HttpStatus.FORBIDDEN);
        return Response.status(Response.Status.FORBIDDEN).entity(dto).build();
    }
}