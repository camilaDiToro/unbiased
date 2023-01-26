package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class ApiErrorExceptionMapper implements ExceptionMapper<ApiErrorException> {

    @Override
    public Response toResponse(ApiErrorException e) {
        ApiErrorDto dto = new ApiErrorDto(e.getApiErrorMessage(), e.getApiCode(), e.getDetails());
        return Response.status(e.getHttpStatus().value()).entity(dto).build();
    }
}