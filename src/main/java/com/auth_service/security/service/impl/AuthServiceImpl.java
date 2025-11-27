package com.auth_service.security.service.impl;

import com.auth_service.constant.ExceptionConstant;
import com.auth_service.dto.UserRegisterDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.enums.UserRole;
import com.auth_service.exception.ExternalAuthServiceException;
import com.auth_service.exception.InvalidRefreshTokenException;
import com.auth_service.exception.UserNotFoundException;
import com.auth_service.mapper.UserMapper;
import com.auth_service.security.model.AuthRequest;
import com.auth_service.security.model.AuthResponse;
import com.auth_service.security.model.CustomUserDetails;
import com.auth_service.security.service.AuthService;
import com.auth_service.security.service.JwtService;
import com.auth_service.service.UserServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            userServiceClient.create(userRequestDto);
        } catch (FeignException.Conflict e) {

            throw new UserNotFoundException(e.getMessage());
        } catch (FeignException e) {

            throw new ExternalAuthServiceException(ExceptionConstant.AUTHENTICATION_SERVICE_UNAVAILABLE);
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) getAuthentication(registerDto.getUsername(), uncachedPassword).getPrincipal();

        return generateTokens(customUserDetails);
    }

    @Override
    public AuthResponse login(AuthRequest request) {

        Authentication authentication = getAuthentication(request.getUsername(), request.getPassword());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return generateTokens(userDetails);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        String extractedUsername = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetailsByLogin = userDetailsService.loadUserByUsername(extractedUsername);

        if (!jwtService.isTokenValid(refreshToken, userDetailsByLogin)) {
            throw new InvalidRefreshTokenException(ExceptionConstant.INVALID_REFRESH_TOKEN);
        }

        return generateTokens(userDetailsByLogin);
    }

    private AuthResponse generateTokens(CustomUserDetails customUserDetails) {

        String accessToken = jwtService.generateToken(customUserDetails, ACCESS_TOKEN_EXPIRATION_TIME);
        String refreshToken = jwtService.generateToken(customUserDetails, REFRESH_TOKEN_EXPIRATION_TIME);

        return new AuthResponse(accessToken, refreshToken);
    }

    private Authentication getAuthentication(String login, String password) {

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );
    }
}
