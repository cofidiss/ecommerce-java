package com.ecommerce.service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
    @Bean
    CommandLineRunner redisInfo(RedisConnectionFactory factory) {
        return args -> {
            System.out.println("REDIS FACTORY = " + factory.getClass());

            if (factory instanceof LettuceConnectionFactory lettuce) {
                System.out.println("REDIS HOST = " + lettuce.getHostName());
                System.out.println("REDIS PORT = " + lettuce.getPort());
            }
        };
    }
}
