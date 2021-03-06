package com.lmk.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmk.model.entity.SysUserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/20
 */
public interface LoginService extends IService<SysUserEntity> {
    boolean selectLoginByUsername(String username, String password, String code, HttpServletRequest request);

    void addRedisCode(String code);

//    void sendEmail(String email);

    Boolean forgetPassword(String username,String emailCode,String password);
}
