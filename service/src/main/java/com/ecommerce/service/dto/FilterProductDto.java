package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class FilterProductDto {
    private String name;
    private Long categoryId;
    private Double minPrice;
    private Double maxPrice;
}
