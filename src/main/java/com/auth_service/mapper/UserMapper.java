package com.auth_service.mapper;

import com.auth_service.dto.UserRegisterDto;
import com.auth_service.dto.UserRequestDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper
        (
                componentModel = "spring",
                unmappedTargetPolicy = ReportingPolicy.ERROR,
                injectionStrategy = InjectionStrategy.CONSTRUCTOR
        )
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    UserRequestDto toUserRequestDto(UserRegisterDto dto);

}
