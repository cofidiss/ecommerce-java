package com.ecommerce.service.implementations;

import com.ecommerce.domain.entity.CategoryEntity;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.repository.interfaces.ICategoryRepository;
import com.ecommerce.repository.interfaces.IProductRepository;
import com.ecommerce.service.dto.CategoryLovDto;
import com.ecommerce.service.dto.FilterProductDto;
import com.ecommerce.service.dto.FilteredProductDto;
import com.ecommerce.service.interfaces.IProductService;
import com.ecommerce.service.mapper.ICategoryMapper;
import com.ecommerce.service.mapper.IProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
IProductRepository productRepository;
    ICategoryRepository categoryRepository;
    ICategoryMapper categoryMapper;
    IProductMapper productMapper;
    public ProductService(ICategoryRepository categoryRepository, ICategoryMapper categoryMapper,
                          IProductRepository productRepository,IProductMapper productMapper){
       this.categoryRepository = categoryRepository;
       this.categoryMapper = categoryMapper;
       this.productRepository = productRepository;
       this.productMapper = productMapper;
    }
    @Override
    public GenericApiResponse<String, Iterable<FilteredProductDto>> GetFilteredProductDtoList(FilterProductDto filter) {
     var response =   new GenericApiResponse<String, Iterable<FilteredProductDto>>();
       var  filterProductModel = productMapper.toFilterProductModel(filter);
      var filteredResults = productRepository.filterProducts(filterProductModel);
        var  filteredProductDtoList = productMapper.toFilteredProductDtoList(filteredResults);
        response.setSuccessData(filteredProductDtoList);
        return response;
    }

    @Override
    public GenericApiResponse<String, Iterable<CategoryLovDto>> getCategoryLov() {
     var genericResponse = new GenericApiResponse<String, Iterable<CategoryLovDto>>();

      var allCategoryEntity =  categoryRepository.findAll();
      var allCategory = new CategoryEntity();
      allCategory.setId(0L);
      allCategory.setName("All");
        allCategoryEntity.add(0,allCategory);
        genericResponse.setSuccessData(categoryMapper.toCategoryLovDtoList(allCategoryEntity));
        return genericResponse;
    }
}
