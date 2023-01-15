package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;
import org.springframework.http.HttpStatus;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class NewsNotFoundExceptionMapper implements ExceptionMapper<NewsNotFoundException> {

    private static final String MESSAGE = "Article not found";

    @Override
    public Response toResponse(NewsNotFoundException e) {
        ApiErrorDto dto = new ApiErrorDto(MESSAGE, ApiErrorCode.NEWS_NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND);
        return Response.status(Response.Status.NOT_FOUND).entity(dto).build();
    }
}
