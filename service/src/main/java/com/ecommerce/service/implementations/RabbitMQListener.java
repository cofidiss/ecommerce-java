package com.ecommerce.service.implementations;

import com.ecommerce.service.config.RabbitConfig;
import com.ecommerce.service.interfaces.IBasketService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQListener {
    IBasketService basketService;
    public RabbitMQListener(IBasketService basketService){
        this.basketService = basketService;
    }
    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE )
    public void handleMail(Object event) {

        // burası başka bir işlem
        // örn: email gönder, stok düş, log at

        System.out.println("Order received: ");
    }
    @RabbitListener(queues = RabbitConfig.CART_QUEUE )
    public void handleCart(Object event) {

        basketService.ClearAllBasketsAndNotifyUsers();

        System.out.println("Order received: ");
    }
}
