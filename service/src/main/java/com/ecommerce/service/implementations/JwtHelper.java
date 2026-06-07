package com.ecommerce.service.implementations;

import com.ecommerce.service.config.JwtConfig;
import com.ecommerce.service.interfaces.IJwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
@Service
public class JwtHelper implements IJwtHelper {
    private JwtConfig jwtConfig;
    public JwtHelper(JwtConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }
    public String generateToken(long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    public Long extractUserId(String token) {
        try {
            return  Long.parseLong(extractAllClaims(token).getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
                return  !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims == null || claims.getExpiration().before(new Date());
    }
}
