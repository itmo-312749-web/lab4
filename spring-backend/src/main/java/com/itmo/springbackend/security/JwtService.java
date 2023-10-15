package com.itmo.springbackend.security;

import com.itmo.springbackend.user.EmailUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration-time}")
    private long jwtExpirationTime;

    @Value("${application.security.jwt.refresh-token.expiration-time}")
    private long refreshTokenExpirationTime;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyByteArray = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByteArray);
    }

    public String generateToken(EmailUserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> additionalClaims, EmailUserDetails userDetails) {
        return buildToken(additionalClaims, userDetails, jwtExpirationTime);
    }

    public String generateRefreshToken(EmailUserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpirationTime);
    }

    private String buildToken(
            Map<String, Object> additionalClaims,
            EmailUserDetails userDetails,
            long expirationTime
    ) {
        return Jwts
                .builder()
                .setClaims(additionalClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, EmailUserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
