package com.auth_service.client.feign;

import com.auth_service.dto.MailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mailing-service")
public interface MailingClient {

    @PostMapping("/mailing/sender")
    void sendMail(@RequestBody MailDto dto);

}
