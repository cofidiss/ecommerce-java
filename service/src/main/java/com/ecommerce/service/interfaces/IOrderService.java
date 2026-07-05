package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.FilterOrderDto;
import com.ecommerce.service.dto.FilteredOrderDto;

import java.util.List;

public interface IOrderService {


      public GenericApiResponse<String,String> CreateOrder(long userId);


    GenericApiResponse<String, List<FilteredOrderDto>> getUserOrder(long userId);

    GenericApiResponse<String, List<FilteredOrderDto>>  filterOrder(FilterOrderDto filter);
}
