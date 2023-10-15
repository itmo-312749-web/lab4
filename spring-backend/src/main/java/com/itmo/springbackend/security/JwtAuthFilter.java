package com.itmo.springbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static String AUTHENTICATION_HEADER = "Authorization";
    private static final String JWT_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader(AUTHENTICATION_HEADER);
        final String jwt;

        if (authenticationHeader == null || !authenticationHeader.startsWith(JWT_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authenticationHeader.substring(JWT_HEADER_PREFIX.length());
        final String userEmail = jwtService.extractEmail(jwt);


    }
}
