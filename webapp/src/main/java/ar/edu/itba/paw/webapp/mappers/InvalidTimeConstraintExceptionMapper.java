package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.InvalidOrderException;
import ar.edu.itba.paw.model.exeptions.InvalidTimeConstraintException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class InvalidTimeConstraintExceptionMapper implements ExceptionMapper<InvalidTimeConstraintException> {

    @Override
    public Response toResponse(InvalidTimeConstraintException e) {
        ApiErrorDto dto = new ApiErrorDto(ApiErrorCode.INVALID_TIME_CONSTRAINT, e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}
