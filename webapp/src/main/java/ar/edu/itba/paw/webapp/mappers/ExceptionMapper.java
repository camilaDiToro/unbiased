package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

//@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        ApiErrorDto dto = new ApiErrorDto(e.toString(), ApiErrorCode.UNKNOWN, e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(dto).build();
    }
}
