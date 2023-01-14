//package ar.edu.itba.paw.webapp.mappers;
//
//import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
//import ar.edu.itba.paw.webapp.auth.exceptions.ApiErrorException;
//import ar.edu.itba.paw.webapp.auth.exceptions.UserDisabeledException;
//import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
//import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;
//
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.ExceptionMapper;
//import javax.ws.rs.ext.Provider;
//
//@Provider
//public class ApiErrorExceptionMapper implements ExceptionMapper<UserDisabeledException> {
//
//    @Override
//    public Response toResponse(UserDisabeledException e) {
//        ApiErrorDto dto = ApiErrorDto.fromApiErrorException(e);
//        return Response.status(e.getHttpStatus()).entity(dto).build();
//    }
//}