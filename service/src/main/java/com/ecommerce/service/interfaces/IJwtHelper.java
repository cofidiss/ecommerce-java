package com.ecommerce.service.interfaces;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public interface IJwtHelper {
    public Long extractUserId(String token);
    public String generateToken(long userId) ;


    public boolean validateToken(String token);

    public boolean isTokenExpired(String token) ;
}
