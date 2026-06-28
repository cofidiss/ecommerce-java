package com.ecommerce.service.implementations;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.domain.util.CustomConstants;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.repository.interfaces.IProductRepository;
import com.ecommerce.service.dto.BasketInfoItemDto;
import com.ecommerce.service.dto.BasketItemDto;
import com.ecommerce.service.interfaces.IBasketService;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IWSMessageHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final IProductRepository productRepository;
    private final SimpMessagingTemplate messagingTemplate;
    public BasketService(RedisTemplate redisTemplate, IJwtHelper jwtHelper, IWSMessageHandler wsMessageHandler, IProductRepository productRepository,SimpMessagingTemplate messagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.jwtHelper = jwtHelper;
        this.wsMessageHandler = wsMessageHandler;
        this.productRepository = productRepository;
        this.messagingTemplate = messagingTemplate;

    }

    @Override
    public GenericApiResponse<String, String> AddBasket(BasketItemDto basketItemDto, long userId) {

        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);

        List<BasketItemDto> currentBasket;

        if (currentBasketObj instanceof List<?> list
                && list.stream().allMatch(BasketItemDto.class::isInstance)) {
            currentBasket = (List<BasketItemDto>) list;
        } else {
            currentBasket = new ArrayList<>();
        }

        Optional<BasketItemDto> optBasket = currentBasket.stream()
                .filter(item -> item.getProductId() == basketItemDto.getProductId())
                .findFirst();

        if (optBasket.isPresent()) {
            BasketItemDto currentBasketDto = optBasket.get();
            currentBasketDto.setQuantity(
                    currentBasketDto.getQuantity() + basketItemDto.getQuantity());

        } else {
            currentBasket.add(basketItemDto);
        }
        redisTemplate.opsForValue().set(redisKey, currentBasket);
        redisTemplate.opsForSet().add(CustomConstants.RedisBasketSetKey, redisKey);
        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, List<BasketInfoItemDto>> GetBasket(long userId) {
        wsMessageHandler.SendMessageToUser(userId, "asds");
        GenericApiResponse<String, List<BasketInfoItemDto>> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);
        var basketItemInfoList = new ArrayList<BasketInfoItemDto>();
        if (currentBasketObj instanceof List<?> list &&
                list.stream().allMatch(BasketItemDto.class::isInstance)) {

            for (var basketItem : list) {
                var product = productRepository.findById(((BasketItemDto) basketItem).getProductId()).get();
                var quantity = ((BasketItemDto) basketItem).getQuantity();
                var basketInfoDto = new BasketInfoItemDto();
                basketInfoDto.setProductId(product.getId());
                basketInfoDto.setQuantity(quantity);
                basketInfoDto.setTotalPrice(product.getPrice() * quantity);
                basketItemInfoList.add(basketInfoDto);
            }
            apiResponse.setSuccessData(basketItemInfoList);
        } else {

            apiResponse.setSuccessData(new ArrayList<BasketInfoItemDto>());
        }
        return apiResponse;
    }

    @Override
    public GenericApiResponse<String, String> DeleteFromBasket(long itemId, long userId) {

        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        var redisKey = CustomUtil.GetRedisBasketKeyForUser(userId);
        Object currentBasketObj = redisTemplate.opsForValue().get(redisKey);
        if (currentBasketObj instanceof List<?> list) {

            var currentBasket = (List<BasketItemDto>) list;
            var optItem = currentBasket.stream().filter(item -> item.getProductId() == itemId).findFirst();

            if (optItem.isPresent()) {

                var itemInBasket = optItem.get();
                itemInBasket.setQuantity(itemInBasket.getQuantity() - 1);
                if (itemInBasket.getQuantity() == 0) {
                    currentBasket.remove(itemInBasket);
                }
            }
            redisTemplate.opsForValue().set(redisKey, currentBasket);
        }


        apiResponse.setSuccessData("item removed");
        return apiResponse;
    }

    public void ClearAllBasketsAndNotifyUsers() {
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

        }
messagingTemplate.convertAndSend("/topic/orderCreated", "Stock numbers are changed and Basket is emptied");
    }

}

