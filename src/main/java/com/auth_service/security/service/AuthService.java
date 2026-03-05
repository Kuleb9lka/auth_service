package com.auth_service.security.service;

import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.security.AuthRequest;
import com.auth_service.dto.security.AuthResponse;

public interface AuthService {

    AuthResponse register(UserRequestDto userDto);

    AuthResponse login(AuthRequest request);

    AuthResponse refreshToken(String refreshToken);
}
