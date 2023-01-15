package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.auth.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.auth.exceptions.ApiErrorException;
import ar.edu.itba.paw.webapp.dto.ApiErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



// https://www.baeldung.com/spring-security-exceptionhandler
// https://github.com/cassiomolin/jersey-jwt-springsecurity/blob/master/src/main/java/com/cassiomolin/example/security/api/jwt/JwtAuthenticationEntryPoint.java
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        ApiErrorDto apiErrorDto;

        if (authException instanceof ApiErrorException) {
            apiErrorDto = ApiErrorDto.fromApiErrorException((ApiErrorException) authException);
        }else{
            apiErrorDto = new ApiErrorDto("Forbidden", ApiErrorCode.FORBIDDEN, authException.getMessage(), HttpStatus.FORBIDDEN);
        }

        response.setStatus(apiErrorDto.getStatus());
        response.setContentType(CustomMediaType.ERROR_V1.getValue());

        mapper.writeValue(response.getWriter(), apiErrorDto);
    }
}