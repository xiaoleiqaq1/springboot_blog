package com.lmk.api.constants;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
public class RedisConstant {
    //此段是后台存进User中redis中的数据
    public static final String SET_USERNAME="string:addSet:username:";
    public static final String SET_EMAIL="string:addSet:email:";
    public static final String STRING_CODE="string:addCode:";
    //此段是前端存进redis中的数据

    public static final String STRING_FRONTEMAIL="string:front:email:";

    //存进session中的key
    public static final String SESSION_KEY="session_key";

    public static final String ZSET_PRAISE = "zset:praise:";
}
