package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class FilteredProductDto {
    private String name;
    private int quantity;
    private long itemId;
    private long price;
}

