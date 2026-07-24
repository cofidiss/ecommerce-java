package com.ecommerce.service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String OrdersEXCHANGE = "orders-exchange";
    public static final String CART_QUEUE  = "order.created.cart";
    public static final String MAIL_QUEUE = "order.created.mail";
    public static final String ORDER_CREATED = "order.created";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(OrdersEXCHANGE);
    }

    @Bean
    Queue orderCreatedQueue()  {
        return QueueBuilder.durable(ORDER_CREATED).build();
    }

    @Bean
    Binding mailBinding() {
        return BindingBuilder.bind(orderCreatedQueue()).to(exchange()).with(ORDER_CREATED);
    }

}
