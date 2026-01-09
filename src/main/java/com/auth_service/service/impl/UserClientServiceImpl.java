package com.auth_service.service.impl;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.feignclient.UserClient;
import com.auth_service.service.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserClient userClient;

    @Override
    public UserAuthDto getByUsername(String username) {
        return userClient.getByUsername(username);
    }

    @Override
    public UserResponseDto create(UserRequestDto requestDto) {
        return userClient.create(requestDto);
    }
}
