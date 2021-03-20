package com.lmk.server.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
public class ShiroUtil {
    //加密算法
    public final static String hashAlgorithmName = "SHA-256";

    //循环次数
    public final static int hashIterations = 16;

    public static String sha256(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }

    public static void main(String[] args) {
        String password = "123456";
        System.out.println(ShiroUtil.sha256(password, "xiaolei"));
    }
}
