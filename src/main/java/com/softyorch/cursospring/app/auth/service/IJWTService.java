package com.softyorch.cursospring.app.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface IJWTService {
    String create(Authentication auth, String username) throws IOException;
    boolean validate(String token);
    Claims getClaims(String token);
    String getUsername(String token);
    Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
    String resolve(String token);
}
