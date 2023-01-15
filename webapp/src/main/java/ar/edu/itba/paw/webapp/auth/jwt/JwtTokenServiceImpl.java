package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.webapp.auth.exceptions.ExpiredJwtTokenException;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidJwtClaimException;
import ar.edu.itba.paw.webapp.auth.exceptions.InvalidJwtTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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

    private final String jwtSecret;
    private final String jwtIssuer;

    private static final long ACCESS_TOKEN_DURATION_SECS = 2 * 60;
    private static final long REFRESH_TOKEN_DURATION_SECS = 12 * 60;

    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String TOKEN_TYPE_CLAIM = "token_type";

    @Autowired
    public JwtTokenServiceImpl(Environment environment) {
        this.jwtSecret = environment.getRequiredProperty("jwt.secret");
        this.jwtIssuer = environment.getRequiredProperty("jwt.issuer");
    }


    @Override
    public String createAccessToken(final UserDetails userDetails) {
        return createToken(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DURATION_SECS * 1000), userDetails, JwtTokenType.ACCESS);
    }

    @Override
    public String createRefreshToken(final UserDetails userDetails) {
        return createToken(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION_SECS * 1000), userDetails, JwtTokenType.REFRESH);
    }

    private String createToken(final Date expiresAt, final UserDetails userDetails, final JwtTokenType tokenType){
       return JWT.create()
               .withJWTId(generateTokenIdentifier())
               .withSubject(userDetails.getUsername())
               .withClaim(AUTHORITIES_CLAIM, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
               .withIssuedAt(new Date())
               .withExpiresAt(expiresAt)
               .withIssuer(jwtIssuer)
               .withClaim(TOKEN_TYPE_CLAIM, tokenType.getType())
               .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }


    @Override
    public JwtTokenDetails validateTokenAndGetDetails(final String token) {
       try {
           final DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtSecret.getBytes())).withIssuer(jwtIssuer).build().verify(token);
           return new JwtTokenDetails.Builder()
                   .withId(decodedJWT.getId())
                   .withEmail(decodedJWT.getSubject())
                   .withAuthorities(decodedJWT.getClaim(AUTHORITIES_CLAIM).asList(String.class))
                   .withIssuedDate(decodedJWT.getIssuedAt())
                   .withExpirationDate(decodedJWT.getExpiresAt())
                   .withToken(token)
                   .withTokenType(JwtTokenType.getByType(decodedJWT.getClaim(TOKEN_TYPE_CLAIM).asString()))
                   .build();
       }catch (TokenExpiredException e){
            throw new ExpiredJwtTokenException(e.getMessage(), e);
       }catch (InvalidClaimException e){
            throw new InvalidJwtClaimException(e.getMessage(), e);
       }catch (Exception e){
            throw new InvalidJwtTokenException(e.getMessage(), e);
       }
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}
