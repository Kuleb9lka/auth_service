package com.auth_service.mapper;

import com.auth_service.constant.ExceptionConstant;
import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.enums.UserRole;
import com.auth_service.enums.UserStatus;
import com.auth_service.exception.MappingException;
import org.springframework.stereotype.Component;

@Component
public class UserProtoMapper {

    public UserAuthDto toAuthDtoFromProto(com.user_service.generated.UserAuthDto dto) {

        return new UserAuthDto(dto.getUsername(), dto.getPassword(), dto.getEmail(), UserRole.valueOf(dto.getRole().name()));
    }

    public UserResponseDto toResponseDtoFromProto(com.user_service.generated.UserResponseDto dto) {

        return new UserResponseDto(
                dto.getId(),
                dto.getUsername(),
                dto.getFirstname(),
                dto.getLastname(),
                dto.getEmail(),
                mapProtoRoleToRole(dto.getRole()),
                mapProtoStatusToStatus(dto.getStatus()));
    }

    public com.user_service.generated.UserRequestDto toProtoRequestDto(UserRequestDto dto) {

        return com.user_service.generated.UserRequestDto.newBuilder()
                .setUsername(dto.getUsername())
                .setPassword(dto.getPassword())
                .setFirstname(dto.getFirstname())
                .setLastname(dto.getLastname())
                .setEmail(dto.getEmail())
                .build();
    }

    private UserRole mapProtoRoleToRole(com.user_service.generated.UserRole role) {

        if (role == null) {

            throw new MappingException(ExceptionConstant.FIELD_CANT_BE_NULL);
        }

        return switch (role) {
            case USER, USER_ROLE_UNSPECIFIED, UNRECOGNIZED -> UserRole.USER;
            case ADMIN -> UserRole.ADMIN;
        };
    }

    private UserStatus mapProtoStatusToStatus(com.user_service.generated.UserStatus status) {

        if (status == null) {

            throw new MappingException(ExceptionConstant.FIELD_CANT_BE_NULL);
        }

        return switch (status) {
            case ACTIVE -> UserStatus.ACTIVE;
            case DISABLED -> UserStatus.DISABLED;
            case NEED_EMAIL_CONFIRMATION, UNRECOGNIZED, USER_STATUS_UNSPECIFIED -> UserStatus.NEED_EMAIL_CONFIRMATION;
        };
    }


}
