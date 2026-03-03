package com.auth_service.feign;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.feign.decoder.UserServiceDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        configuration = UserServiceDecoder.class
)
public interface UserClient {

    @GetMapping("/users/{id}")
    UserResponseDto getById(@PathVariable Long id);

    @GetMapping("/users/by-username/{username}")
    UserAuthDto getByUsername(@PathVariable String username);

    @GetMapping("/users/by-token/{token}")
    UserResponseDto getUserByConfirmationToken(@PathVariable String token);

    @PostMapping("/users/new")
    UserResponseDto create(@RequestBody UserRequestDto userRequestDto);

    @GetMapping("/users/generate-token/{userId}")
    String generateEmailVerificationToken(@PathVariable Long userId);

    @GetMapping("/users/confirm-email/{token}")
    void verifyUserEmail(@PathVariable String token);


}

