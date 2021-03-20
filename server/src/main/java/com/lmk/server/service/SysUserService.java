package com.lmk.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmk.api.utils.PageUtil;
import com.lmk.model.entity.SysUserEntity;

import java.util.Map;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/17
 */
public interface SysUserService extends IService<SysUserEntity> {

    PageUtil findAll(Map<String,Object> map);
//    List<SysUserEntity> findAll(Map<String,Object> map);

    int add(SysUserEntity sysUserEntity);

//    int delete2(Long id);

    int update(SysUserEntity sysUserEntity);

    SysUserEntity getById(Long id);

    Boolean addRedisUserName(String username);

    Boolean addRedisUserEmail(String email);

}
