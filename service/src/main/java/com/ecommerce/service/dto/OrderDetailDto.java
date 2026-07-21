package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private long id;
    private String userName;
    private long productId;
    private double quantity;
    private double amount;
}
