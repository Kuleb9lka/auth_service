package com.auth_service.service;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;

public interface UserServiceClient {

    UserAuthDto getByUsername(String username);

    UserResponseDto create(UserRequestDto requestDto);
}
