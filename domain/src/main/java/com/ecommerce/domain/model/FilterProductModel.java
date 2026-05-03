package com.ecommerce.domain.model;

import lombok.Data;

@Data
public class FilterProductModel {
    private String name;
    private long categoryId;
    private Double minPrice;
    private Double maxPrice;
}
