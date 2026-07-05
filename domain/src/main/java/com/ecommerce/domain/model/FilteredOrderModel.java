package com.ecommerce.domain.model;

import lombok.Data;

@Data
public class FilteredOrderModel {
    private String userName;
    private long orderId;
    private double orderAmount;
}
