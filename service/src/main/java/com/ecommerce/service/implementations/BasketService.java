package com.ecommerce.service.implementations;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.domain.util.CustomConstants;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.BasketItemDto;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IWSMessageHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BasketService implements IBasketService {

    private RedisTemplate redisTemplate;
    private IJwtHelper jwtHelper;
    private final IWSMessageHandler wsMessageHandler;

    public  BasketService(RedisTemplate redisTemplate, IJwtHelper jwtHelper,IWSMessageHandler wsMessageHandler){
this.redisTemplate = redisTemplate;
this.jwtHelper = jwtHelper;
this.wsMessageHandler = wsMessageHandler;

}

    @Override
    public GenericApiResponse<String, String> AddBasket(BasketItemDto basketItemDto, long userId) {

        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);

        List<BasketItemDto> currentBasket;

        if (currentBasketObj instanceof List<?> list) {
            currentBasket =  (List<BasketItemDto>) list;
        } else {
            currentBasket = new ArrayList<>();
        }

        Optional<BasketItemDto> optBasket = currentBasket.stream()
                .filter(item -> item.getItemId() == basketItemDto.getItemId())
                .findFirst();

        if (optBasket.isPresent()) {
            BasketItemDto currentBasketDto = optBasket.get();
            currentBasketDto.setQuantity(
                    currentBasketDto.getQuantity() + basketItemDto.getQuantity());

        }
        else {
            currentBasket.add(basketItemDto);
        }
        redisTemplate.opsForValue().set(redisKey,currentBasket);
        redisTemplate.opsForSet().add(CustomConstants.RedisBasketSetKey, currentBasket);
        return apiResponse;
    }

    @Override
    public GenericApiResponse< String,List<BasketItemDto>> GetBasket(long userId) {
        wsMessageHandler.SendMessageToUser(userId,"asds");
        GenericApiResponse< String,List<BasketItemDto>> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);
        if (currentBasketObj instanceof List<?> list) {

            apiResponse.setSuccessData((List<BasketItemDto>)list);
        }
        else {

            apiResponse.setSuccessData(new ArrayList<BasketItemDto>());
        }
        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, String> DeleteFromBasket(long itemId, long userId) {

        GenericApiResponse< String,String> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);
        if (currentBasketObj instanceof List<?> list) {

           var currentBasket =(List<BasketItemDto>)list;
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
private void ClearAllBasketsAndNotifyUsers(){
    Set<String> allBasketKeys =
            redisTemplate.opsForSet().members(CustomConstants.RedisBasketSetKey);

    if (allBasketKeys == null) {
        return;
    }

    for (String key : allBasketKeys) {
        Optional<Long> userIdOpt = CustomUtil.ExtractUserIdSafe(key);

        if (userIdOpt.isEmpty()) {
            continue;
        }

        Long userId = userIdOpt.get();

        redisTemplate.delete(key);
        redisTemplate.opsForSet()
                .remove(CustomConstants.RedisBasketSetKey, key);
        wsMessageHandler.SendMessageToUser(userId,"Stock numbers are changed and Basket is emptied");
    }

 }

}
}
