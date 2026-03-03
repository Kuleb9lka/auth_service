package com.auth_service.service.impl;

import com.auth_service.constant.AuthConstant;
import com.auth_service.dto.MailDto;
import com.auth_service.dto.UserAuthDto;
import com.auth_service.dto.UserRequestDto;
import com.auth_service.dto.UserResponseDto;
import com.auth_service.exception.user_service.EmailConfirmationTokenExpirationException;
import com.auth_service.feign.UserClient;
import com.auth_service.producer.MailProducer;
import com.auth_service.security.service.AuthHelper;
import com.auth_service.service.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserClient userClient;

    private final MailProducer mailProducer;

    private final AuthHelper authHelper;

    @Override
    public UserAuthDto getByUsername(String username) {
        return userClient.getByUsername(username);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {

        return userClient.create(requestDto);
    }

    @Override
    public String generateEmailConfirmationToken(Long userId) {
        return userClient.generateEmailVerificationToken(userId);
    }

    @Override
    public UserResponseDto getUserByConfirmationToken(String token) {

        return userClient.getUserByConfirmationToken(token);
    }

    @Override
    public void verifyUserEmail(String token) {

        try{

            userClient.verifyUserEmail(token);

        } catch (EmailConfirmationTokenExpirationException e) {

            UserResponseDto userByConfirmationToken = userClient.getUserByConfirmationToken(token);

            String newToken = userClient.generateEmailVerificationToken(userByConfirmationToken.getId());

            String newTokenConfirmationUrl = authHelper.constructEmailConfirmationUrl(newToken);

            String reconfirmationMailText = authHelper.constructEmailTextWithUrl(AuthConstant.RECONFIRMATION_EMAIL_TEXT, newTokenConfirmationUrl);

            MailDto mailDto = new MailDto(userByConfirmationToken.getEmail(), AuthConstant.EMAIL_RECONFIRMATION, reconfirmationMailText);

            mailProducer.sendMail(mailDto);

            throw e;
        }

    }

}
