package com.ecommerce.service.implementations;

import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.domain.model.GenericApiResponse;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.dto.UserDto;
import com.ecommerce.service.interfaces.IJwtHelper;
import com.ecommerce.service.interfaces.IUserService;
import com.ecommerce.service.mapper.IUserMapper;
import com.ecommerce.repository.interfaces.IUserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private RedisTemplate redisTemplate;
    private IUserRepository userRepository;
    private IJwtHelper jwtHelper;
    private IUserMapper userMapper;

    public UserService(IUserRepository userRepository, IJwtHelper jwtHelper, RedisTemplate redisTemplate, IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
        this.userMapper =  userMapper;
    }

    @Override
    public GenericApiResponse<String, String> login(String username, String password) {
        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        Optional<UserEntity> userOpt = userRepository.findByName(username);

        if(userOpt.isPresent() && verifyPassword(password, userOpt.get().getPassword())){
            var userEntity = userOpt.get();
            apiResponse.setSuccessData(jwtHelper.generateToken( userEntity.getId()));
            redisTemplate.opsForValue().set(String.valueOf(userEntity.getId()),userEntity);
        }
        else {
            apiResponse.setErrorData("login unsuccess");
        }
        return apiResponse; // Şifre doğrulaması
    }
    private boolean verifyPassword(String rawpassword, String encodedPassword) {
        return Objects.equals(CustomUtil.hashPassword(rawpassword), encodedPassword);
    }
@Transactional
    @Override
    public GenericApiResponse<String, String> signUp(SignUpRequestDto signUpRequest) {
        GenericApiResponse<String, String> apiResponse = new GenericApiResponse<>();
        // DTO'yu Entity'ye dönüştür
        UserEntity userEntityToInsert = userMapper.toEntity(signUpRequest);

        // Veritabanına ekle
        userRepository.save(userEntityToInsert);

        Optional<UserEntity> userOpt = userRepository.findByName(signUpRequest.getUsername());

        if(userOpt.isPresent()){
            apiResponse.setSuccessData(jwtHelper.generateToken( userOpt.get().getId()));
            redisTemplate.opsForValue().set(String.valueOf( userOpt.get().getId()),userMapper.toDto(userOpt.get()));
        }
        return apiResponse;
    }

    @Override
    public UserDto getUserDto(long userId) {

     var optUserEntity =   userRepository.findById(userId);
        return optUserEntity.map(userEntity -> userMapper.toDto(userEntity)).orElse(null);

    }
}
