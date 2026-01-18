package com.ecommerce.service.implementations;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.AddBasketDto;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IOrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private RedisTemplate redisTemplate;

    public OrderService(RedisTemplate redisTemplate){
this.redisTemplate = redisTemplate;

}


    @Override
    public GenericApiResponse<String, String> CreateOrder(long userId) {
        return null;
    }
}
