package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenDetails;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

// Checked resources:
//  - https://gist.github.com/OmarElgabry/bfbbbe5d082e003ce1f007c5b8b513ac#file-jwttokenauthenticationfilter-java
//  - https://www.youtube.com/watch?v=VVn9OG9nfH0


public class JwtFilter extends OncePerRequestFilter {

    // Un usuario puede autenticarse accediendo a cualquier endpoint de la api. NO existe un endpoint de login
    // Pasos en caso de requerir autenticacion:

    //    |                       CLIENTE                       |                   SERVIDOR
    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    // 1. | hace un request                                     | devuelve 403/401
    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    // 2. | a. recibe el 403/401                                | a. el request pasa por este filter, detecta que el header Authorization comienza con basic.
    //    | b. muestra el formulario de login                   | b. Logea al usuario
    //    | c. envia el header Authorization con basic/digest   | c. crea el token jwt, deja pasar el request
    //    | request + Authorization: basic user:pass            | d. agrega el token en el response (auth y refresh)
    //    |                                                     |
    // -----------------------------------------------------------------------------------------------------------------------------------------------------
    // 3. | almacena los tokens y los envia en cada request     | detecta que el request tiene el header Authorization y que comienza con bearer.
    //-----------------------------------------------------------------------------------------------------------------------------------------------------
    // 4. | Si el token expiro envia el refresh token           | genera un nuevo token y lo incluye en la respuesta

    // Informacion que se podria incluir en el token jwt: id del usuario, self url, roles


    private final PawUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public JwtFilter( PawUserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(authHeader);

        if(authHeader == null){
            filterChain.doFilter(request, response);
            return;
        }

        if(authHeader.startsWith("Basic ")){
            final Credentials credentials = AuthUtils.getCredentialsFromBasic(authHeader);
            System.out.println(credentials.getEmail());
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                JwtTokenDetails jwtTokenDetails = new JwtTokenDetails(userDetails, userDetailsService.getUser());
                response.addHeader("x-access-token", jwtTokenService.createAccessToken(jwtTokenDetails));
                response.addHeader("x-refresh-token", jwtTokenService.createRefreshToken(jwtTokenDetails));
            }catch (Exception e){
                response.addHeader("x-jwt-error-message", e.getLocalizedMessage());
                SecurityContextHolder.clearContext();
            }

        } else if(authHeader.startsWith("Bearer ")) {
            //TODO
        }

        filterChain.doFilter(request, response);
        return;
    }
}
