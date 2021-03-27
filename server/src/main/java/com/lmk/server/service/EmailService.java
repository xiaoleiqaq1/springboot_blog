package com.lmk.server.service;

import org.springframework.mail.SimpleMailMessage;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/27
 */
public interface EmailService {

    void sendEmail(SimpleMailMessage message);
}
