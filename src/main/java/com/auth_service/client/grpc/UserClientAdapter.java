package com.auth_service.client.grpc;

import com.auth_service.client.UserClient;
import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.mapper.UserProtoMapper;
import com.user_service.generated.ConfirmationToken;
import com.user_service.generated.UserId;
import com.user_service.generated.UserServiceGrpc;
import com.user_service.generated.Username;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserClient {

    @GrpcClient(value = "user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    private final UserProtoMapper userMapper;


    @Override
    public UserAuthDto getByUsername(String username) {

        Username request = Username.newBuilder().setUsername(username).build();

        com.user_service.generated.UserAuthDto response = userStub.getByUsername(request);

        return userMapper.toAuthDtoFromProto(response);
    }

    @Override
    public UserResponseDto getUserByConfirmationToken(String token) {

        ConfirmationToken request = ConfirmationToken.newBuilder().setToken(token).build();

        com.user_service.generated.UserResponseDto response = userStub.getUserByConfirmationToken(request);

        return userMapper.toResponseDtoFromProto(response);
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {

        com.user_service.generated.UserRequestDto request = userMapper.toProtoRequestDto(userRequestDto);

        com.user_service.generated.UserResponseDto response = userStub.create(request);

        return userMapper.toResponseDtoFromProto(response);
    }

    @Override
    public String generateEmailConfirmationToken(Long userId) {

        UserId request = UserId.newBuilder().setId(userId).build();

        ConfirmationToken confirmationToken = userStub.generateEmailVerificationToken(request);

        return confirmationToken.getToken();
    }

    @Override
    public void verifyUserEmail(String token) {

        ConfirmationToken request = ConfirmationToken.newBuilder().setToken(token).build();

        userStub.verifyUserEmail(request);
    }
}
