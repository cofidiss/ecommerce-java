package com.ecommerce.service.mapper;

import com.ecommerce.domain.entity.UserEntity;
import com.ecommerce.domain.util.CustomUtil;
import com.ecommerce.service.dto.SignUpRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = CustomUtil.class)
public interface IUserMapper {
    @Mapping(target = "name", source = "username")
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    UserEntity toEntity(SignUpRequestDto dto);

}
