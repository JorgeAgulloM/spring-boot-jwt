package com.softyorch.cursospring.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityConfig {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                .requestMatchers("/detail/**").hasAnyRole("USER")
                .requestMatchers("/uploads/**").hasAnyRole("USER")
                .requestMatchers("/form/**").hasAnyRole("ADMIN")
                .requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
                .requestMatchers("/factura/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated())
                .formLogin(fl -> fl.loginPage("/login").permitAll())
                .logout(lOut -> lOut
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        //.logoutRequestMatcher(new AntPathRequestMatcher("/listar"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                ).httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User
                .withUsername("jorge")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build());

        manager.createUser(User
                .withUsername("admin")
                .password(passwordEncoder().encode("12345"))
                .roles("ADMIN", "USER")
                .build());

        return manager;
    }





}
