package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.api.CustomMediaType;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(value = { CustomMediaType.ERROR_V1 })
public class InvalidCategoryExceptionMapper {
}
