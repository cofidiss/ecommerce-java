package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.dto.UserDto;

public interface IUserService {

    public GenericApiResponse<String,String> login(String username, String password);
    public GenericApiResponse<String,String> signUp(SignUpRequestDto signUpRequest);
    public UserDto getUserDto(long userId);

    boolean isAdmin(Long userId);
}
