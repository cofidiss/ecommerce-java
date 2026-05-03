package com.ecommerce.domain.model;

import lombok.Data;

@Data
public class FilteredProductModel {
    private String name;
    private Double quantity;
    private Long itemId;
    private Double price;
    public FilteredProductModel(){


    }
    public FilteredProductModel(String name,Double quantity, Long id,Double price  ){
        this.name = name;
        this.quantity = quantity;
        this.itemId = id;
        this.price = price;
    }
}
