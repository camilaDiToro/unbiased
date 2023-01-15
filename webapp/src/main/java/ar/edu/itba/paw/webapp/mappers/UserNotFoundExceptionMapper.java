package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {

    private static final String MESSAGE = "An user with id '%d' was not found";

    @Override
    public Response toResponse(UserNotFoundException e) {
        SimpleMessageDto dto = SimpleMessageDto.fromString(String.format(MESSAGE, 5));
        return Response.status(Response.Status.NOT_FOUND).entity(dto).build();
    }
}