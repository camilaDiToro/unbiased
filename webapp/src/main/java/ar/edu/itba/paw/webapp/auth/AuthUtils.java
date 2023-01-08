package ar.edu.itba.paw.webapp.auth;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthUtils {

    private AuthUtils() {
        // Utility class
    }

    // https://stackoverflow.com/questions/16000517/how-to-get-password-from-http-basic-authentication
    public static Credentials getCredentialsFromBasic(String basic){
        // Authorization:Basic email:password
        String base64Credentials = basic.substring("Basic ".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return new Credentials(values[0], values[1]);
    }

}
