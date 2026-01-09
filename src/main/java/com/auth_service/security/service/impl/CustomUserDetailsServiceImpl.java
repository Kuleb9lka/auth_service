package com.auth_service.security.service.impl;

import com.auth_service.dto.UserAuthDto;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.dto.security.CustomUserDetails;
import com.auth_service.service.UserClientService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserClientService userClientService;


    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAuthDto byUsername;

        try {

            log.info("Trying to get user by username^ {}", username);

            byUsername = userClientService.getByUsername(username);

        } catch (FeignException.NotFound e) {

            log.error("User not found", e);

            throw new UserNotFoundException(e.getMessage());
        }

        log.info("User was successfully found");

        return new CustomUserDetails(
                byUsername.getUsername(),
                byUsername.getPassword(),
                byUsername.getEmail(),
                Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(byUsername.getRole()))));
    }
}
