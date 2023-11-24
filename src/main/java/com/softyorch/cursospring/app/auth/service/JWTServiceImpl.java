package com.softyorch.cursospring.app.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softyorch.cursospring.app.auth.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JWTServiceImpl implements IJWTService {

    public static final byte[] SECRET = Base64Util.encode("MiclaveSecreta123456789MiclaveSecreta123456789").getBytes();
    public static final long EXPIRATION_DATE = 1000 * 60 * 10;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES = "authorities";
    public static final String AUTHORITY = "authority";

    @Override
    public String create(Authentication auth, String username) throws IOException {

        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET);

        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        ClaimsBuilder claims = Jwts.claims();
        claims.add(AUTHORITIES, new ObjectMapper().writeValueAsString(roles));

        return Jwts.builder()
                .claims(claims.build())
                .subject(username)
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .compact();
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.fillInStackTrace();
        }

        return false;
    }

    @Override
    public Claims getClaims(String token) {
        SecretKey secret = Keys.hmacShaKeyFor(SECRET);
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(resolve(token))
                .getPayload();
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(
            String token
    ) throws IOException {
        Object roles = getClaims(token).get(AUTHORITIES);
        return Arrays.asList(
                new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                        .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class)
        );
    }

    @Override
    public String resolve(String token) {
        return (token != null && token.startsWith(TOKEN_PREFIX)) ?
                token.replace(TOKEN_PREFIX, "") :
                null;
    }
}
