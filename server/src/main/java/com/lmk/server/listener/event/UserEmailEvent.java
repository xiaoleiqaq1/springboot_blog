package com.lmk.server.listener.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.mail.SimpleMailMessage;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
public class UserEmailEvent extends ApplicationEvent {

    private SimpleMailMessage message;

    public UserEmailEvent(Object source, SimpleMailMessage message) {
        super(source);
        this.message = message;
    }

    public SimpleMailMessage getMessage() {
        return message;
    }

    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }
}
