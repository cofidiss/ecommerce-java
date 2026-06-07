package com.ecommerce.domain.util;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

import org.mapstruct.Named;

@Component
public class CustomUtil {
    static MessageDigest digest ;
    static {
        try {
            digest = MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("SHA-512 algoritması bulunamadı", ex);
        }

    }
    @Named("hashPassword")
    public static String  hashPassword(String password) {
        if (password == null) {
            return null;
        }

            // SHA-512 hash işlemi

            byte[] hash = digest.digest(password.getBytes());
            // Hash'i Base64 olarak döndür
            return HexFormat.of().formatHex(hash);

    }

    public static String GetRedisBasketKeyForUser(long userId){

return "user:" + userId + ":basket";
    }
    public static String GetRedisWsKeyForUser(long userId){

        return "user:" + userId + ":ws";
    }
    public static Optional<Long> ExtractUserIdSafe(String key) {
        if (key == null) return Optional.empty();

        String[] parts = key.split(":");
        if (parts.length == 3 && "user".equals(parts[0]) && "basket".equals(parts[2])) {
            return Optional.of(Long.parseLong(parts[1]));
        }
        return Optional.empty();
    }

    public static String GetRedisKeyForSession(long userId){
        return "user:" + userId + ":session";

    }
}
