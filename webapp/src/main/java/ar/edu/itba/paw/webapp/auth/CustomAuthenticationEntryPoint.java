package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.auth.models.ApiErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiErrorDetails errorDetails = new ApiErrorDetails();

        /*if (authException instanceof InvalidAuthenticationTokenException) {
            status = HttpStatus.UNAUTHORIZED;
            errorDetails.setTitle(authException.getMessage());
            errorDetails.setMessage(authException.getCause().getMessage());
        } else {
            status = HttpStatus.FORBIDDEN;
            errorDetails.setTitle(status.getReasonPhrase());
            errorDetails.setMessage(authException.getMessage());
        }*/

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        errorDetails.setStatus(status.value());
        errorDetails.setPath(request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorDetails);
    }
}