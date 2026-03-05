package com.auth_service.service;

import com.auth_service.dto.MailDto;

public interface MailingClientService {

    void send(MailDto dto);
}
