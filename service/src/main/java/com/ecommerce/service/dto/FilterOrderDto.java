package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class FilterOrderDto {

    private long userId;
    private double minAmount;
    private double maxAmount;
}
