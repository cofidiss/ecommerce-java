package com.ecommerce.service.dto;


import lombok.Data;

@Data
public class UserDto {

    private long id;

    private long roleId;

    private String name;


    private String password;


    private String phone;


    private String address;
}
