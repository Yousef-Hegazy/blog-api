package com.yousef.blog.services.impl;

import com.yousef.blog.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private final Long jwtExpirationMs = 86400000L; // 1 day

    @Override
    public UserDetails authenticate(String email, String password) {
        var res = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (res.isAuthenticated()) {
            return userDetailsService.loadUserByUsername(email);
        }
        return null;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public UserDetails validateToken(String token) {
        if (isTokenExpired(token)) {
            return null;
        }
        var username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private Claims extractTokenClaims(String token) {
        return Jwts.parser()
                .decryptWith(getSigningKey())
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }

    private String extractUsername(String token) {
        return extractTokenClaims(token).getSubject();
    }

    private Date extractExpiration(String token) {
        return extractTokenClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSigningKey() {
        var keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
