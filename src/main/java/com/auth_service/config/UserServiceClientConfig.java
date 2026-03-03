package com.auth_service.config;

import com.auth_service.feign.decoder.UserServiceDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceClientConfig {

    @Bean
    public ErrorDecoder userServiceErrorDecoder(){
        return new UserServiceDecoder();
    }
}
