package com.softyorch.cursospring.app.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softyorch.cursospring.app.auth.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (!requiresAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }

        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("MiclaveSecreta123456789MiclaveSecreta123456789"));

        boolean tokenValid = false;
        Claims token = null;
        try {
            token = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(header.replace("Bearer ", ""))
                    .getPayload();
            tokenValid = true;
        } catch (JwtException | IllegalArgumentException e) {
               e.fillInStackTrace();
        }

        UsernamePasswordAuthenticationToken auth = null;
        if (tokenValid) {
            String username = token.getSubject();
            Object roles = token.get("authorities"); //authority

            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                    new ObjectMapper()
                            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                            .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class)
            );

            auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);

    }

    protected boolean requiresAuthentication(String header) {
        return header != null && header.startsWith("Bearer ");
    }
}
