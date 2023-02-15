package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class CommentNotFoundExceptionMapper implements ExceptionMapper<CommentNotFoundException> {

    @Override
    public Response toResponse(CommentNotFoundException e) {
        ApiErrorDto dto = new ApiErrorDto(ApiErrorCode.COMMENT_NOT_FOUND, e.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(dto).build();
    }
}