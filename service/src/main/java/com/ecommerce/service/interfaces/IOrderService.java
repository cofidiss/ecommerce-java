package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;

public interface IOrderService {


      public GenericApiResponse<String,String> CreateOrder(long userId);


}
