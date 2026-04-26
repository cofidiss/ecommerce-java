package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.BasketItemDto;

import java.util.List;

public interface IBasketService {


      public GenericApiResponse<String,String> AddBasket(BasketItemDto basketItemDto, long userId);
    public GenericApiResponse<String,List<BasketItemDto>> GetBasket(long userId);
    public GenericApiResponse<String,String> DeleteFromBasket( long itemId,long userId);
    public void ClearAllBasketsAndNotifyUsers();

}
