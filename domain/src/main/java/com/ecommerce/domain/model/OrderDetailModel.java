package com.ecommerce.domain.model;

import lombok.Data;

@Data
public class OrderDetailModel {
    private long id;
    private String userName;
    private long productId;
    private double quantity;
    private double amount;
}
