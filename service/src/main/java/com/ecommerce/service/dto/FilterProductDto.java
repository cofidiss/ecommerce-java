package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class FilterProductDto {
    private String name;
    private long categoryId;
    private double minPrice;
    private double maxPrice;
}
