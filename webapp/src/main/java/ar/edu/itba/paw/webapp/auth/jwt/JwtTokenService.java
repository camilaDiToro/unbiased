package ar.edu.itba.paw.webapp.auth.jwt;

public interface JwtTokenService {
    String createAccessToken(final JwtTokenDetails jwtTokenDetails);
    String createRefreshToken(final JwtTokenDetails jwtTokenDetails);
    JwtTokenDetails getTokenDetails(final String token);
}
