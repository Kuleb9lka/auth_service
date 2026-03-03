package com.auth_service.security.service;

public interface AuthHelper {

    String constructEmailConfirmationUrl(String token);

    String constructEmailTextWithUrl(String mailText, String token);
}
