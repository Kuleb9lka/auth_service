package com.auth_service.exception.handler;

import com.auth_service.dto.ExceptionResponseDto;
import com.auth_service.enums.ExceptionStatus;
import com.auth_service.exception.InvalidRefreshTokenException;
import com.auth_service.exception.mail_service.EmailNotFoundException;
import com.auth_service.exception.user_service.EmailConfirmationTokenExpirationException;
import com.auth_service.exception.user_service.EmailAlreadyActivatedException;
import com.auth_service.exception.user_service.UserCredentialsConflictException;
import com.auth_service.exception.user_service.UserEmailConfirmationNotFoundException;
import com.auth_service.exception.user_service.UserNotFoundException;
import com.auth_service.exception.user_service.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidRefreshTokenException(InvalidRefreshTokenException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponseDto(ExceptionStatus.INVALID_REFRESH_TOKEN.name(), exception.getMessage()));
    }

    @ExceptionHandler(UserCredentialsConflictException.class)
    public ResponseEntity<ExceptionResponseDto> handleRegistrationDetailsConflictException(UserCredentialsConflictException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponseDto(ExceptionStatus.USER_CREDENTIALS_CONFLICT.name(), exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotFoundException(UserNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(ExceptionStatus.USER_NOT_FOUND.name(), exception.getMessage()));
    }

    @ExceptionHandler(EmailConfirmationTokenExpirationException.class)
    public ResponseEntity<ExceptionResponseDto> handleEmailConfirmationTokenExpirationException(EmailConfirmationTokenExpirationException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(ExceptionStatus.USER_EMAIL_CONFIRMATION_TOKEN_EXPIRED.name(), exception.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyActivatedException.class)
    public ResponseEntity<ExceptionResponseDto> handleEmailIsAlreadyActivatedException(EmailAlreadyActivatedException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(ExceptionStatus.EMAIL_IS_ALREADY_ACTIVATED.name(), exception.getMessage()));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleEEmailNotFoundException(EmailNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(ExceptionStatus.EMAIL_NOT_FOUND.name(), exception.getMessage()));
    }

    @ExceptionHandler(UserEmailConfirmationNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserEmailConfirmationNotFoundException(UserEmailConfirmationNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(ExceptionStatus.USER_EMAIL_CONFIRMATION_NOT_FOUND.name(), exception.getMessage()));
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserServiceException(UserServiceException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponseDto(ExceptionStatus.USER_SERVICE_UNEXPECTED_ERROR.name(), exception.getMessage()));
    }
}
