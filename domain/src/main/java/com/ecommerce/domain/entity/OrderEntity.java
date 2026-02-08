package com.ecommerce.domain.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order")
public class OrderEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;
}
