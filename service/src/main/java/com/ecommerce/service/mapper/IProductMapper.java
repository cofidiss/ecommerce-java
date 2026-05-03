package com.ecommerce.service.mapper;

import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.domain.model.FilterProductModel;
import com.ecommerce.domain.model.FilteredProductModel;
import com.ecommerce.service.dto.FilterProductDto;
import com.ecommerce.service.dto.FilteredProductDto;
import com.ecommerce.service.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {

    FilterProductModel toFilterProductModel(FilterProductDto filterProductDto);

    FilteredProductDto toFilteredProductDto(FilteredProductModel filteredProductModel);
    List<FilteredProductDto> toFilteredProductDtoList(List<FilteredProductModel> filteredProductModelList);
}
