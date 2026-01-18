package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class AddBasketDto {

    private int quantity;
    private long itemId;
}
