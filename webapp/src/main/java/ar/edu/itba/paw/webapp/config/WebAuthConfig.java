package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.jwt.JwtFilter;
import ar.edu.itba.paw.webapp.auth.LoginFailureHandler;
import ar.edu.itba.paw.webapp.auth.CustomUserDetailsService;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Value("${security.key.remeberme}")
    private String rememberMeKey;

    @Value("${spa.url}")
    private String spaUrl;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(spaUrl);
        return corsConfiguration;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setDefaultTargetUrl("/");
//        successHandler.setAlwaysUseDefaultTargetUrl(false);
//        successHandler.setTargetUrlParameter("redirectTo");

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                    //.antMatchers("/login", "/create").anonymous()
                    .antMatchers (GET, "/users").authenticated()
                    .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('OWNER')")
                    .antMatchers("/owner/**").hasRole("OWNER")
                    .antMatchers("/create_article","/change-upvote","/change-downvote","/news/create",
                            "/news/{\\d+}/delete", "/news/{\\d+}/comment", "/news/{\\d+}/save").authenticated()
                    .antMatchers("/**").permitAll()
                    .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().addFilterBefore(new JwtFilter(userDetailsService, authenticationManager(), jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }
}