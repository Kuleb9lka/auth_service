package com.auth_service.security.service.impl;

import com.auth_service.constant.ExceptionConstant;
import com.auth_service.dto.UserRegisterDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.enums.UserRole;
import com.auth_service.exception.ExternalAuthServiceException;
import com.auth_service.exception.InvalidRefreshTokenException;
import com.auth_service.exception.RegistrationDetailsConflictException;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.mapper.UserMapper;
import com.auth_service.dto.security.AuthRequest;
import com.auth_service.dto.security.AuthResponse;
import com.auth_service.dto.security.CustomUserDetails;
import com.auth_service.security.service.AuthService;
import com.auth_service.security.service.JwtService;
import com.auth_service.service.UserServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.access-expiration-time}")
    private Integer ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-expiration-time}")
    private Integer REFRESH_TOKEN_EXPIRATION_TIME;
    private final UserServiceClient userServiceClient;

    private final UserMapper userMapper;

    private final PasswordEncoder encoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsServiceImpl userDetailsService;

    @Override
    public AuthResponse register(UserRegisterDto registerDto) {

        String uncachedPassword = registerDto.getPassword();

        registerDto.setPassword(encoder.encode(registerDto.getPassword()));

        UserRequestDto userRequestDto = userMapper.toUserRequestDto(registerDto);

        userRequestDto.setRole(UserRole.USER);

        try {

            log.info("Trying to create a user");

            userServiceClient.create(userRequestDto);

        } catch (FeignException.Conflict e) {

            throw new RegistrationDetailsConflictException(e.getMessage());

        } catch (FeignException e) {

            throw new ExternalAuthServiceException(ExceptionConstant.AUTHENTICATION_SERVICE_UNAVAILABLE);
        }

        log.info("Getting custom user details for username: {}", registerDto.getUsername());

        CustomUserDetails customUserDetails = (CustomUserDetails) getAuthentication(registerDto.getUsername(), uncachedPassword).getPrincipal();

        log.info("Generating access and refresh tokens");

        return generateTokens(customUserDetails);
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        log.info("Getting authentication by username: {}", request.getUsername());

        Authentication authentication = getAuthentication(request.getUsername(), request.getPassword());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        log.info("Generating access and refresh tokens");

        return generateTokens(userDetails);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        String extractedUsername = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetailsByLogin = userDetailsService.loadUserByUsername(extractedUsername);

        log.info("Checking validity of refresh token for user: {}", extractedUsername);

        if (!jwtService.isTokenValid(refreshToken, userDetailsByLogin)) {
            throw new InvalidRefreshTokenException(ExceptionConstant.INVALID_REFRESH_TOKEN);
        }

        log.info("Generating new tokens");

        return generateTokens(userDetailsByLogin);
    }

    private AuthResponse generateTokens(CustomUserDetails customUserDetails) {

        String accessToken = jwtService.generateToken(customUserDetails, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtService.generateToken(customUserDetails, REFRESH_TOKEN_EXPIRATION_TIME);

        return new AuthResponse(accessToken, refreshToken);
    }

    private Authentication getAuthentication(String login, String password) {
        log.debug("Attempting to authenticate user: {}", login);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );
    }
}
