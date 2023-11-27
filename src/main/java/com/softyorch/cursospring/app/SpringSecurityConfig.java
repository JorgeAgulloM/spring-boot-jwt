package com.softyorch.cursospring.app;

import com.softyorch.cursospring.app.auth.filter.JWTAuthenticationFilter;
import com.softyorch.cursospring.app.auth.filter.JWTAuthorizationFilter;
import com.softyorch.cursospring.app.auth.service.IJWTService;
import com.softyorch.cursospring.app.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserDetailsService userDetailService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private IJWTService jwtService;

    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/listar",
                                        "/locale",
                                        "/api/**"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtService))
                .addFilter(new JWTAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(), jwtService))
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)//desactiva csrf para formularios, ya que vamos a trabajar con REST.
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //Deshabilita el uso de sesiones

        return http.build();
    }
}
