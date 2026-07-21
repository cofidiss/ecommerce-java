package com.ecommerce.service.mapper;

import com.ecommerce.domain.entity.CategoryEntity;
import com.ecommerce.domain.model.FilterOrderModel;
import com.ecommerce.domain.model.FilteredOrderModel;
import com.ecommerce.domain.model.OrderDetailModel;
import com.ecommerce.service.dto.CategoryLovDto;
import com.ecommerce.service.dto.FilterOrderDto;
import com.ecommerce.service.dto.FilteredOrderDto;
import com.ecommerce.service.dto.OrderDetailDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderMapper {
    FilteredOrderDto toFilteredOrderDto(FilteredOrderModel model);
    List<FilteredOrderDto> toFilteredOrderDtoList(List<FilteredOrderModel> entityList);

    FilterOrderModel toFilterOrderModel(FilterOrderDto dto);

    OrderDetailDto toOrderDetailDto(OrderDetailModel model);
    List<OrderDetailDto> toOrderDetailDtoList(List<OrderDetailModel> model);


}
