package com.example.reserve.utils;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    static private String jwtSecret;
    static private long jwtExpirationMs;

    @Value("${jwt.secret}")
    public void setJwtSecretValue(String jwtSecretValue) {
        JwtTokenProvider.jwtSecret = jwtSecretValue;
    }

    @Value("${jwt.expiration}")
    public void setJwtExpirationMsValue(long jwtExpirationMsValue) {
        JwtTokenProvider.jwtExpirationMs = jwtExpirationMsValue;
    }

    static public String generateJwtToken(String data) {
        Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(data)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    public static String parseJwtToken(String jwtToken) {
        try {
            Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
