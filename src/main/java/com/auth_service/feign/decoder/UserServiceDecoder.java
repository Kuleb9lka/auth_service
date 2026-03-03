package com.auth_service.feign.decoder;

import com.auth_service.constant.ExceptionConstant;
import com.auth_service.dto.ExceptionResponseDto;
import com.auth_service.enums.ExceptionStatus;
import com.auth_service.exception.user_service.EmailConfirmationTokenExpirationException;
import com.auth_service.exception.user_service.EmailAlreadyActivatedException;
import com.auth_service.exception.user_service.UserCredentialsConflictException;
import com.auth_service.exception.user_service.UserEmailConfirmationNotFoundException;
import com.auth_service.exception.user_service.UserNotFoundException;
import com.auth_service.exception.user_service.UserServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class UserServiceDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public Exception decode(String s, Response response) {

        try(InputStream inputStream = response.body().asInputStream()){

            ExceptionResponseDto exceptionResponse = mapper.readValue(inputStream, ExceptionResponseDto.class);

            log.warn("UserService returned Exception: {} for method {}", exceptionResponse.getErrorStatus(), s);

            ExceptionStatus exceptionStatus = ExceptionStatus.valueOf(exceptionResponse.getErrorStatus());

            return switch (exceptionStatus){
                case EMAIL_IS_ALREADY_ACTIVATED -> new EmailAlreadyActivatedException(exceptionResponse.getErrorMessage());
                case USER_NOT_FOUND -> new UserNotFoundException(exceptionResponse.getErrorMessage());
                case EMAIL_IS_ALREADY_EXIST, USERNAME_IS_ALREADY_EXIST -> new UserCredentialsConflictException(exceptionResponse.getErrorMessage());
                case USER_EMAIL_CONFIRMATION_NOT_FOUND -> new UserEmailConfirmationNotFoundException(exceptionResponse.getErrorMessage());
                case USER_EMAIL_CONFIRMATION_TOKEN_EXPIRED -> new EmailConfirmationTokenExpirationException(exceptionResponse.getErrorMessage());
                default -> new UserServiceException(exceptionResponse.getErrorMessage());
            };
        } catch (Exception e){

            log.error("Failed to decode error from UserService", e);

            return new UserServiceException(ExceptionConstant.USER_SERVICE_UNEXPECTED);
        }
    }
}
