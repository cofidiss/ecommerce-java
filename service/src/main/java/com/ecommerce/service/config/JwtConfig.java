package com.ecommerce.service.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfig {

    private String secret;

    private String expirationTime;

    public Long getExpirationTime() {
        return Long.parseLong(expirationTime);
    }
}
