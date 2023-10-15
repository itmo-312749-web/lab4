package com.itmo.springbackend.security;

import com.itmo.springbackend.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final static String AUTHENTICATION_HEADER = "Authorization";
    private static final String JWT_HEADER_PREFIX = "Bearer ";

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authenticationHeader = request.getHeader(AUTHENTICATION_HEADER);
        final String jwt;
    }
}
