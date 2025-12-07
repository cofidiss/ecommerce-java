package com.ecommerce.service.interfaces;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.SignUpRequestDto;

public interface IUserService {

    public GenericApiResponse<String,String> login(String username, String password);
    public GenericApiResponse<String,String> signUp(SignUpRequestDto signUpRequest);
}
