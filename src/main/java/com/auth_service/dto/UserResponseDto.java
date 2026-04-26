package com.auth_service.dto;

import com.auth_service.enums.UserRole;
import com.auth_service.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private UserRole role;

    private UserStatus status;
}
