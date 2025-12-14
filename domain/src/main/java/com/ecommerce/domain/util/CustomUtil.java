package com.ecommerce.domain.util;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
            return Base64.getEncoder().encodeToString(hash);

    }
}
