package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.AddBasketDto;

import java.util.List;

public interface IOrderService {


      public GenericApiResponse<String,String> CreateOrder(long userId);


}
