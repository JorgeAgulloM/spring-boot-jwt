package com.softyorch.cursospring.app.auth.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.softyorch.cursospring.app.models.entity.SysUser;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username != null && password != null) {
            logger.info("Username from request parameter (form-data): ".concat(username));
            logger.info("Password from request parameter (form-data): ".concat(password));
        } else {
            SysUser user;
            try {
                user = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);

                username = user.getUsername();
                password = user.getPassword();

                logger.info("Username from request InputStream (raw): ".concat(username));
                logger.info("Password from request InputStream (raw): ".concat(password));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        UsernamePasswordAuthenticationToken authToken =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {

        //String username = authResult.getName();
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();

        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("MiclaveSecreta123456789MiclaveSecreta123456789"));
        logger.warn("SecretKey: ".concat(secretKey.getAlgorithm()));

        int minutes = 10;
        long expiredTimeMinutes = 1000 * 60 * minutes;

        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        ClaimsBuilder claims = Jwts.claims();
        claims.add("authorities", new ObjectMapper().writeValueAsString(roles));

        String token = Jwts.builder()
                .claims(claims.build())
                .subject(username)
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMinutes))
                .compact();
        logger.warn("Token: ".concat(token));

        response.addHeader("Authorization", "Bearer ".concat(token));

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("user", user);
        body.put("message", String.format("Hola %s, lo has conseguido!", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error en la autenticación. Usuario o contraseña incorrectos!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }
}
