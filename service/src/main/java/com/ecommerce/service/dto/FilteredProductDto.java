package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class FilteredProductDto {
    private String name;
    private Double quantity;
    private Long itemId;
    private Double price;
}

