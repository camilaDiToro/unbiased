package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class JwtTokenServiceImpl implements JwtTokenService{

    private final Environment environment;
    private final String jwtSecret;

    private static final long ACCESS_TOKEN_DURATION_SECS = 2 * 60;
    private static final long REFRESH_TOKEN_DURATION_SECS = 2 * 24 * 60;

    @Autowired
    public JwtTokenServiceImpl(Environment environment) {
        this.environment = environment;
        this.jwtSecret = environment.getRequiredProperty("jwt.secret");
    }


    @Override
    public String createAccessToken(final JwtTokenDetails jwtTokenDetails) {
        return createToken(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION_SECS * 1000), jwtTokenDetails);
    }

    @Override
    public String createRefreshToken(final JwtTokenDetails jwtTokenDetails) {
        return createToken(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION_SECS * 1000), jwtTokenDetails);
    }

    private String createToken(final Date expiresAt, final JwtTokenDetails jwtTokenDetails){
        final User user = jwtTokenDetails.getUser();
       return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(expiresAt)
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                .withClaim("roles", user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()))
               .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }

    @Override
    public JwtTokenDetails getTokenDetails(final String token) {
        //TODO
        return null;
    }
}
