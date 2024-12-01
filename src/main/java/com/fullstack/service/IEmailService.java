package com.fullstack.service;

import com.fullstack.model.EmailModel;

public interface IEmailService {

    void sendEmail(EmailModel emailModel);
}