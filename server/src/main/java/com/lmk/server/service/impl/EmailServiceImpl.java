package com.lmk.server.service.impl;

import com.lmk.server.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @auth: lmk
 * @Description: 注册完后发送邮箱
 * @date: 2021/3/24
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(SimpleMailMessage message) {
        mailSender.send(message);
    }
}
