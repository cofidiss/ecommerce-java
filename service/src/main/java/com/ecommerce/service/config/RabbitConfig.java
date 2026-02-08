package com.ecommerce.service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "orders-exchange";
    public static final String QUEUE = "order-created-queue";
    public static final String ROUTING_KEY = "order.created";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }
}
