package com.ecommerce.service.interfaces;

import com.ecommerce.domain.entity.CategoryEntity;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.CategoryLovDto;
import com.ecommerce.service.dto.FilterProductDto;
import com.ecommerce.service.dto.FilteredProductDto;

public interface IProductService {

    public GenericApiResponse<String,Iterable<FilteredProductDto>> GetFilteredProductDtoList(FilterProductDto filter);

    public GenericApiResponse<String,Iterable<CategoryLovDto>> getCategoryLov();
}
