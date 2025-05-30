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

    // It's generally better practice to retrieve the key from application properties
    // For demonstration, keeping it as is.
    private final String key = "bA9$Q2wE5zX7#vP0sDfGhJkLmN!rT6uY";
    private final SecretKey sec = Keys.hmacShaKeyFor(key.getBytes());

    // Modified generateToken to accept UserDetails
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Add roles and permissions as a claim
        // Spring Security roles/authorities are usually stored as a list of strings
        // representing the authority names (e.g., "ROLE_ADMIN", "READ_PRIVILEGE")
        claims.put("authorities", userDetails.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList()));

        return Jwts.builder()
            .setClaims(claims) // Set the claims
            .setSubject(userDetails.getUsername()) // Use UserDetails.getUsername()
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
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

    // You might also need to extract authorities when validating/parsing the token
    // This method is for demonstration, you'd likely do this in a JWT filter
    public java.util.List<String> extractAuthorities(String token) {
        Claims claims = extractToken(token);
        // Claims.get("authorities") will return an ArrayList if it was stored as a List
        return (java.util.List<String>) claims.get("authorities");
    }

    public boolean validate(String userName, UserDetails usd, String token) {
        // Validation should also consider authorities if they are crucial for your application
        // For now, keeping your original validation
        return (userName.equals(usd.getUsername())) && (!isExpired(token));
    }

    private boolean isExpired(String token) {
        return extractToken(token).getExpiration().before(new Date());
    }
}