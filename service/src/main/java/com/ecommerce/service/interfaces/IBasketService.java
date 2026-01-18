package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.AddBasketDto;

import java.util.List;

public interface IBasketService {


      public GenericApiResponse<String,String> AddBasket(AddBasketDto addBasketDto, long userId);
    public GenericApiResponse<String,List<AddBasketDto>> GetBasket( long userId);
    public GenericApiResponse<String,String> DeleteFromBasket( long itemId,long userId);


}
