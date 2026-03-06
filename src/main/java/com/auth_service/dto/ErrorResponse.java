package com.auth_service.dto;

import com.auth_service.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private ErrorCode errorCode;

    private String message;

    private int status;

    private Instant timestamp;
}
