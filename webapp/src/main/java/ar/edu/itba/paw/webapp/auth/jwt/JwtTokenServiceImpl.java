package ar.edu.itba.paw.webapp.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public String createAccessToken(final UserDetails userDetails) {
        return createToken(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION_SECS * 1000), userDetails);
    }

    @Override
    public String createRefreshToken(final UserDetails userDetails) {
        return createToken(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION_SECS * 1000), userDetails);
    }

    private String createToken(final Date expiresAt, final UserDetails userDetails){
       return JWT.create()
               .withSubject(userDetails.getUsername())
               .withExpiresAt(expiresAt)
               .withIssuedAt(new Date())
               .withNotBefore(new Date())
               .withClaim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
               .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }


    //TODO: BETTER TOKEN VALIDATON
    @Override
    public JwtTokenDetails validateTokenAndGetDetails(final String token) {
       final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtSecret.getBytes())).build().verify(token);

        final List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
        return new JwtTokenDetails.Builder().withAuthorities(authorities).withEmail(decodedJWT.getSubject()).build();
    }

    @Override
    public void authenticateFromDetails(final UserDetails userDetails) {

    }
}
