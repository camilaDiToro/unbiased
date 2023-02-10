package ar.edu.itba.paw.webapp.mappers;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final String MESSAGE = "Web Application Exception";

    @Override
    public Response toResponse(WebApplicationException e) {
        ApiErrorDto dto = new ApiErrorDto( MESSAGE, ApiErrorCode.WEB_APP_ERROR, e.getMessage());
        return Response.status(e.getResponse().getStatus()).entity(dto).build();
    }
}