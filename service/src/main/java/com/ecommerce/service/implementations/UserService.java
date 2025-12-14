package com.ecommerce.service.implementations;

import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IUserService;
import cpm.ecommerce.repository.interfaces.IUserRepository;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Optional;

public class UserService implements IUserService {
    private RedisTemplate redisTemplate;
    private IUserRepository userRepository;
    private IJwtHelper jwtHelper;

    public UserService(IUserRepository userRepository, IJwtHelper jwtHelper, RedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GenericApiResponse<String, String> login(String username, String password) {
        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        Optional<UserEntity> userOpt = userRepository.findByName(username);

        if(userOpt.isPresent() && verifyPassword(password, userOpt.get().getPassword())){
            var userEntity = userOpt.get();
            apiResponse.setSuccessData(jwtHelper.generateToken( userEntity.getId()));
            redisTemplate.opsForValue().set(userEntity.getId(),userEntity);
        }
        else {
            apiResponse.setErrorData("login unsuccess");
        }
        return apiResponse; // Şifre doğrulaması
    }
    private boolean verifyPassword(String rawpassword, String encodedPassword) {
        return Objects.equals(CustomUtil.hashPassword(rawpassword), encodedPassword);
    }

    @Override
    public GenericApiResponse<String, String> signUp(SignUpRequestDto signUpRequest) {
        return null;
    }
}
