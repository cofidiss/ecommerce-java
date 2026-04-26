package com.ecommerce.service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String OrdersEXCHANGE = "orders-exchange";
    public static final String CART_QUEUE  = "order.created.cart";
    public static final String MAIL_QUEUE = "order.created.mail";


    @Bean
    TopicExchange exchange() {
        return new TopicExchange(OrdersEXCHANGE);
    }

    @Bean
    Queue orderCreatedMailQueue() {
        return QueueBuilder.durable("order.created.mail").build();
    }

    @Bean
    Queue orderCreatedCartQueue()  {
        return QueueBuilder.durable("order.created.cart").build();

    }

    @Bean
    Binding mailBinding(FanoutExchange exchange) {
        return BindingBuilder.bind(orderCreatedMailQueue()).to(exchange);
    }

    @Bean
    Binding cartBinding(FanoutExchange exchange) {
        return BindingBuilder.bind(orderCreatedCartQueue()).to(exchange);
    }
}
