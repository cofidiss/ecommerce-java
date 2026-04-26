package com.ecommerce.domain.model;

public class FilteredProductModel {
    private String name;
    private int quantity;
    private long itemId;
    private long price;
    public FilteredProductModel(){


    }
    public FilteredProductModel(String name,int quantity, long itemId,long price  ){
        this.name = name;
        this.quantity = quantity;
        this.itemId = itemId;
        this.  price = price;


    }
}
