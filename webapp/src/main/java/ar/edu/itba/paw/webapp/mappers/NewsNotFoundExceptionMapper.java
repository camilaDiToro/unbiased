package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class NewsNotFoundExceptionMapper implements ExceptionMapper<NewsNotFoundException> {

    private static final String MESSAGE = "An article with id '%d' was not found";

    @Override
    public Response toResponse(NewsNotFoundException e) {
        SimpleMessageDto dto = SimpleMessageDto.fromString(String.format(MESSAGE, 5));
        return Response.status(Response.Status.NOT_FOUND).entity(dto).build();
    }
}
