package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    //                        new ApiErrorDto(
//                        "Validation error",
//                        ApiErrorCode.VALIDATION,
//                        violation.getPropertyPath().toString() + " " + violation.getConstraintDescriptor().getAttributes()

    @Override
    @Produces(value = { CustomMediaType.ERROR_V1 })
    public Response toResponse(final ConstraintViolationException exception) {

        final List<ApiErrorDto> errors =  exception.getConstraintViolations()
                .stream().map((violation)-> ApiErrorDto.fromConstraintViolation(violation)).collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<List<ApiErrorDto>>(errors) {}).build();
    }
}
