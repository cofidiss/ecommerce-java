package com.ecommerce.service.implementations;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.repository.interfaces.ICategoryRepository;
import com.ecommerce.service.dto.CategoryLovDto;
import com.ecommerce.service.dto.FilterProductDto;
import com.ecommerce.service.dto.FilteredProductDto;
import com.ecommerce.service.interfaces.IProductService;
import com.ecommerce.service.mapper.ICategoryMapper;

public class ProductService implements IProductService {

    ICategoryRepository categoryRepository;
    ICategoryMapper categoryMapper;
    public ProductService(ICategoryRepository categoryRepository, ICategoryMapper categoryMapper){
       this.categoryRepository = categoryRepository;
       this.categoryMapper = categoryMapper;
    }
    @Override
    public GenericApiResponse<String, Iterable<FilteredProductDto>> GetFilteredProductDtoList(FilterProductDto filter) {


        return null;
    }

    @Override
    public GenericApiResponse<String, Iterable<CategoryLovDto>> getCategoryLov() {
     var genericResponse = new GenericApiResponse<String, Iterable<CategoryLovDto>>();

      var allCategoryEntity =  categoryRepository.findAll();;
        genericResponse.setSuccessData(categoryMapper.toCategoryLovDtoList(allCategoryEntity));
        return genericResponse;
    }
}
