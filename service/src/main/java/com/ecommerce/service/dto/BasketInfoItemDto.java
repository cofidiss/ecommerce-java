package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class BasketInfoItemDto {
    private int quantity;
    private long productId;
    private double TotalPrice;
}
