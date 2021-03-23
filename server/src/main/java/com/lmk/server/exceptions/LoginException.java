package com.lmk.server.exceptions;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/22
 */
public class LoginException extends RuntimeException{
    public LoginException(String message) {
        super(message);
    }
}
