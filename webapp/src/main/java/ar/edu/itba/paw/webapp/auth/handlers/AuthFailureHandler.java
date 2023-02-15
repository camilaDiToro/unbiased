package ar.edu.itba.paw.webapp.auth.handlers;

import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorExceptionInt;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ApiErrorDto apiErrorDto;

        if (authException instanceof ApiErrorExceptionInt) {
            apiErrorDto = ApiErrorDto.fromApiErrorException((ApiErrorExceptionInt) authException);
        }else{
            apiErrorDto = new ApiErrorDto(ApiErrorCode.UNAUTHORIZED, authException.getMessage());
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CustomMediaType.ERROR_V1);

        mapper.writeValue(response.getWriter(), apiErrorDto);
    }
}