package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.InvalidFilterException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class InvalidFilterExceptionMapper implements ExceptionMapper<InvalidFilterException> {

    @Override
    public Response toResponse(InvalidFilterException e) {
        ApiErrorDto dto = new ApiErrorDto(ApiErrorCode.INVALID_FILTER, e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
    }
}
