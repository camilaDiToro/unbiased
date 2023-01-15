package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.CustomAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.filters.AbstractAuthFilter;
import ar.edu.itba.paw.webapp.auth.handlers.AuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.handlers.AuthSuccessHandler;
import ar.edu.itba.paw.webapp.auth.handlers.CustomAccessDeniedHandler;
import ar.edu.itba.paw.webapp.auth.LoginFailureHandler;
import ar.edu.itba.paw.webapp.auth.CustomUserDetailsService;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthProvider;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AuthFailureHandler authFailureHandler;
    @Autowired
    private AuthSuccessHandler authSuccessHandler;
    @Autowired
    private CustomAuthenticationEntryPoint authEntryPoint;


    @Value("${security.key.remeberme}")
    private String rememberMeKey;

    @Value("${spa.url}")
    private String spaUrl;

    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(jwtAuthProvider);
    }

    @Bean
    public AbstractAuthFilter abstractAuthFilter() throws Exception {
        AbstractAuthFilter abstractAuthFilter = new AbstractAuthFilter();
        abstractAuthFilter.setAuthenticationManager(authenticationManagerBean());
        abstractAuthFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        abstractAuthFilter.setAuthenticationFailureHandler(authFailureHandler);
        return abstractAuthFilter;
    }

//    @Bean
//    public CorsConfiguration corsConfiguration() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin(spaUrl);
//        return corsConfiguration;
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("x-auth-token", "authorization", "X-Total-Pages", "Content-Disposition", "Link"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public ObjectMapper objectMapper() {
        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jacksonMessageConverter.getObjectMapper();

        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return jacksonMessageConverter.getObjectMapper();
    }


    @Override
    protected void configure(final HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDeniedHandler()).and()
                        .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                    //.antMatchers("/login", "/create").anonymous()
                    .antMatchers (PUT, "/api/users/{\\d+}").authenticated()
                    .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('OWNER')")
                    .antMatchers("/owner/**").hasRole("OWNER")
                    .antMatchers("/create_article","/change-upvote","/change-downvote","/news/create",
                            "/news/{\\d+}/delete", "/news/{\\d+}/comment", "/news/{\\d+}/save").authenticated()
                    .antMatchers("/api/**").permitAll()
                    .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().addFilterBefore(abstractAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf()
                .disable();
    }
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }
}