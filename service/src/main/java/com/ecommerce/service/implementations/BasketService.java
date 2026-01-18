package com.ecommerce.service.implementations;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.AddBasketDto;
import com.ecommerce.service.interfaces.IBasketService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService implements IBasketService {

    private RedisTemplate redisTemplate;

    public  BasketService(RedisTemplate redisTemplate){
this.redisTemplate = redisTemplate;

}

    @Override
    public GenericApiResponse<String, String> AddBasket(AddBasketDto addBasketDto, long userId) {

        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);

        List<AddBasketDto> currentBasket;

        if (currentBasketObj instanceof List<?> list) {
            currentBasket =  (List<AddBasketDto>) list;
        } else {
            currentBasket = new ArrayList<>();
        }

        Optional<AddBasketDto> optBasket = currentBasket.stream()
                .filter(item -> item.getItemId() == addBasketDto.getItemId())
                .findFirst();

        if (optBasket.isPresent()) {
            AddBasketDto currentBasketDto = optBasket.get();
            currentBasketDto.setQuantity(
                    currentBasketDto.getQuantity() + addBasketDto.getQuantity());

        }
        else {
            currentBasket.add(addBasketDto);
        }
        redisTemplate.opsForValue().set(redisKey,currentBasket);
        return apiResponse;
    }

    @Override
    public GenericApiResponse< String,List<AddBasketDto>> GetBasket(long userId) {
        GenericApiResponse< String,List<AddBasketDto>> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);
        if (currentBasketObj instanceof List<?> list) {

            apiResponse.setSuccessData((List<AddBasketDto>)list);
        }
        else {

            apiResponse.setSuccessData(new ArrayList<AddBasketDto>());
        }
        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, String> DeleteFromBasket(long itemId, long userId) {

        GenericApiResponse< String,String> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);
        if (currentBasketObj instanceof List<?> list) {

           var currentBasket =(List<AddBasketDto>)list;
         var optItem =  currentBasket.stream().filter(item -> item.getItemId() == itemId).findFirst();

         if(optItem.isPresent()){

         var itemInBasket =    optItem.get();
             itemInBasket.setQuantity(itemInBasket.getQuantity() -1);
             if(itemInBasket.getQuantity() == 0){
                 currentBasket.remove(itemInBasket);
             }
         }
            redisTemplate.opsForValue().set(redisKey,currentBasket);
        }
        apiResponse.setSuccessData("item removed");
        return apiResponse;
    }

}
