package com.ecommerce.service.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private int roleId;
    private String username;
    private String password;
    private String phone;
    private String address;
}
