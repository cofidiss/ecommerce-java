package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.model.FilterProductModel;
import com.ecommerce.domain.model.FilteredProductModel;

import java.util.List;

public interface IProductRepositoryCustom {
    List<FilteredProductModel> filterProducts(FilterProductModel filter);
}
