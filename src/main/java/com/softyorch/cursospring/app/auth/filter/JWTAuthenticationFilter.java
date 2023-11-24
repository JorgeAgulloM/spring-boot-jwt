package com.softyorch.cursospring.app.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import java.io.IOException;
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
        username = (username != null) ? username.trim() : "";

        String password = obtainPassword(request);
        password = (password != null) ? password : "";

        if (!username.isEmpty() || !password.isEmpty()) {
            logger.info("Username desde request parameter (form-data): ".concat(username));
            logger.info("Password desde request parameter (form-data): ".concat(password));
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

        String token = Jwts.builder().subject(username).signWith(secretKey).compact();
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
}
