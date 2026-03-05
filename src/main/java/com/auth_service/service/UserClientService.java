package com.auth_service.service;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;

public interface UserClientService {

    UserAuthDto getByUsername(String username);

    UserResponseDto createUser(UserRequestDto requestDto);

    String generateEmailConfirmationToken(Long id);

    UserResponseDto getUserByConfirmationToken(String token);

    void verifyUserEmail(String token);
}
