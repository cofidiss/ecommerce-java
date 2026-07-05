package com.ecommerce.domain.model;

import lombok.Data;

@Data
public class FilterOrderModel {

    private Long userId;
    private Double minAmount;
    private Double maxAmount;
}
