package com.lmk.server.utils;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/22
 */

import java.security.SecureRandom;
import java.util.Random;

/**
 * 作用：用于发送短信验证码
 * 使用场景：找回密码
 */
public class CodeUtil {

    private static final String SYMBOLS = "0123456789"; // 数字

    // 字符串
    // private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    public static void main(String[] args) {
        System.out.println(getCode());
    }

    /**
     * 获取长度为4的随机数字
     * 随机数字
     */
    public static String getCode() {
        char[] nonceChars = new char[4];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}