package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class FilteredOrderDto {

    private String userName;
    private long orderId;
    private double orderAmount;
}
