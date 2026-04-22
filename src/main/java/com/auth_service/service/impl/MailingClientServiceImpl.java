package com.auth_service.service.impl;

import com.auth_service.dto.MailDto;
import com.auth_service.client.feign.MailingClient;
import com.auth_service.service.MailingClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailingClientServiceImpl implements MailingClientService {

    private final MailingClient mailingClient;


    @Override
    public void send(MailDto dto) {

        log.info("Trying to send mail");

        mailingClient.sendMail(dto);

        log.info("Mail was successfully sent");
    }
}
