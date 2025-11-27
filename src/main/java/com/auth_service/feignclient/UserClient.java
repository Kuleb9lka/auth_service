package com.auth_service.feignclient;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/{id}")
    UserResponseDto getById(@PathVariable("id") Long id);

    @GetMapping("/auth")
    UserAuthDto getByUsername(@RequestParam("username") String username);

    @PostMapping
    UserResponseDto create(@RequestBody UserRequestDto userRequestDto);
}

