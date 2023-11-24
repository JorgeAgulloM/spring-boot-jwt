package com.softyorch.cursospring.app.auth.filter;

import com.softyorch.cursospring.app.auth.service.IJWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static com.softyorch.cursospring.app.auth.service.JWTServiceImpl.HEADER_STRING;
import static com.softyorch.cursospring.app.auth.service.JWTServiceImpl.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final IJWTService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, IJWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (!requiresAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = null;
        if (jwtService.validate(header)) {
            auth = new UsernamePasswordAuthenticationToken(
                    jwtService.getUsername(header),
                    null,
                    jwtService.getRoles(header)
            );
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);

    }

    protected boolean requiresAuthentication(String header) {
        return header != null && header.startsWith(TOKEN_PREFIX);
    }
}
