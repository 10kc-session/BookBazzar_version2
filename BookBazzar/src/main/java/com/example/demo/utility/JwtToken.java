package com.example.demo.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtToken {

    private final String key = "bA9$Q2wE5zX7#vP0sDfGhJkLmN!rT6uY";
    private final SecretKey sec = Keys.hmacShaKeyFor(key.getBytes());

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList()));

        return Jwts.builder()
            .setClaims(claims) 
            .setSubject(userDetails.getUsername()) 
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) 
            .signWith(sec, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims extractToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(sec)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserName(String token) {
        return extractToken(token).getSubject();
    }

    public java.util.List<String> extractAuthorities(String token) {
        Claims claims = extractToken(token);
        // Claims.get("authorities") will return an ArrayList if it was stored as a List
        return (java.util.List<String>) claims.get("authorities");
    }

    public boolean validate(String userName, UserDetails usd, String token) {
        return (userName.equals(usd.getUsername())) && (!isExpired(token));
    }

    private boolean isExpired(String token) {
        return extractToken(token).getExpiration().before(new Date());
    }
}