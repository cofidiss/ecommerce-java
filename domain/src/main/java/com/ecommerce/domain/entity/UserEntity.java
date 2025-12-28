package com.ecommerce.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "role_id", nullable = false)
    private long roleId;
    @Column(name = "name", nullable = false,length = 255)
    private String name;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "address", nullable = true)
    private String address;
}
