package com.ecommerce.service.implementations;

import com.ecommerce.service.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQListener {
    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handle(Object event) {

        // burası başka bir işlem
        // örn: email gönder, stok düş, log at

        System.out.println("Order received: ");
    }
}
