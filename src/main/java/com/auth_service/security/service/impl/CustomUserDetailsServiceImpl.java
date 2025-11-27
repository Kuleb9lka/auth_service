package com.auth_service.security.service.impl;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.security.model.CustomUserDetails;
import com.auth_service.service.UserServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAuthDto byUsername;

        try {
            byUsername = userServiceClient.getByUsername(username);
        } catch (FeignException.NotFound e) {

            throw new UserNotFoundException(e.getMessage());
        }

        return new CustomUserDetails(
                byUsername.getUsername(),
                byUsername.getPassword(),
                byUsername.getEmail(),
                Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(byUsername.getRole()))));
    }
}
