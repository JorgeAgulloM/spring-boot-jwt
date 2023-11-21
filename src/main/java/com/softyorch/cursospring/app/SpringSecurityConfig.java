package com.softyorch.cursospring.app;

import com.softyorch.cursospring.app.auth.handler.LoginSuccessHandler;
import com.softyorch.cursospring.app.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaUserDetailsService userDetailService;

    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/locale").permitAll()
                        /*.requestMatchers("/detail/**").hasAnyRole("USER")*/
                        /*.requestMatchers("/uploads/**").hasAnyRole("USER")*/
                        /*.requestMatchers("/form/**").hasAnyRole("ADMIN")*/
                        /*.requestMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
                        /*.requestMatchers("/factura/**").hasAnyRole("ADMIN")*/
                        .anyRequest().authenticated())
                .formLogin(fl -> fl
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll())
                .logout(lOut -> lOut
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        //.logoutRequestMatcher(new AntPathRequestMatcher("/listar"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .exceptionHandling(((eh) -> eh.accessDeniedPage("/error_403")))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

/*    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

*//*        manager.createUser(User
                .withUsername("jorge")
                .password(passwordEncoder.encode("12345"))
                .roles("USER")
                .build());

        manager.createUser(User
                .withUsername("admin")
                .password(passwordEncoder.encode("12345"))
                .roles("ADMIN", "USER")
                .build());*//*

        return manager;
    }*/


}
