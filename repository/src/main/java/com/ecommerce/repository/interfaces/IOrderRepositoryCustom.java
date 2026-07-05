package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.model.FilterOrderModel;
import com.ecommerce.domain.model.FilteredOrderModel;

import java.util.List;

public interface IOrderRepositoryCustom {

    List<FilteredOrderModel>  customFilterOrder(FilterOrderModel filterOrderModel);
}
