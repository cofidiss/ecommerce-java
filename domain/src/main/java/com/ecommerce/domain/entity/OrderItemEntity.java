package com.ecommerce.domain.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order_id", nullable = false)
    private long orderId;
    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "quantity", nullable = false)
    private long quantity;
}
