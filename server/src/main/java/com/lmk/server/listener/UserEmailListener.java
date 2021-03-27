package com.lmk.server.listener;

import com.lmk.server.listener.event.UserEmailEvent;
import com.lmk.server.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
@Component
public class UserEmailListener implements ApplicationListener<UserEmailEvent> {

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(UserEmailEvent userEmailEvent) {
        SimpleMailMessage message = userEmailEvent.getMessage();
        emailService.sendEmail(message);
    }
}
