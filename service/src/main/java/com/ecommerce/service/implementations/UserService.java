package com.ecommerce.service.implementations;

import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.interfaces.IUserService;
import cpm.ecommerce.repository.interfaces.IUserRepository;

public class UserService implements IUserService {

    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GenericApiResponse<String, String> login(String username, String password) {
        return null;
    }

    @Override
    public GenericApiResponse<String, String> signUp(SignUpRequestDto signUpRequest) {
        return null;
    }
}
