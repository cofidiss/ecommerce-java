package com.ecommerce.service.mapper;

import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.SignUpRequestDto;
import com.ecommerce.service.dto.UserDto;
import com.ecommerce.service.dto.UserLovDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = CustomUtil.class)
public interface IUserMapper {
    @Mapping(target = "name", source = "username")
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    UserEntity toEntity(SignUpRequestDto dto);

    UserDto toDto(UserEntity entity);

    UserLovDto toUserLovDto(UserEntity entity);
    List<UserLovDto> toUserLovListDto(List<UserEntity> entity);
}
