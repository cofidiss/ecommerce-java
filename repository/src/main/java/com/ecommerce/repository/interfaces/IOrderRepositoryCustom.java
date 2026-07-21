package com.ecommerce.repository.interfaces;

import com.ecommerce.domain.model.FilterOrderModel;
import com.ecommerce.domain.model.FilteredOrderModel;
import com.ecommerce.domain.model.OrderDetailModel;

import java.util.List;

public interface IOrderRepositoryCustom {

    List<FilteredOrderModel>  customFilterOrder(FilterOrderModel filterOrderModel);

    List<OrderDetailModel>  getOrderDetailModel(Long orderId);
}
