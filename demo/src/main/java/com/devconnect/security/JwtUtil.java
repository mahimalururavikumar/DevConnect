package com.devconnect.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    public String generateToken(String email) {

        //HOUR
        long EXPIRATION = 1000 * 60 * 60;
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        String SECRET = "ravi_spring_security_password123@gmail.in";
        return Keys.hmacShaKeyFor((SECRET.getBytes()));
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
