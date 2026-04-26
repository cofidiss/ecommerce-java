package com.ecommerce.service.mapper;

import com.ecommerce.domain.entity.CategoryEntity;
import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.service.dto.CategoryLovDto;
import com.ecommerce.service.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ICategoryMapper {

    CategoryLovDto toCategoryLovDto(CategoryEntity entity);
    List<CategoryLovDto> toCategoryLovDtoList(List<CategoryEntity> entityList);
}
