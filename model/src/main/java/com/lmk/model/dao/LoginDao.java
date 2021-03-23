package com.lmk.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmk.model.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/22
 */
@Mapper
public interface LoginDao extends BaseMapper<SysUserEntity> {
    SysUserEntity selectLoginByUsername(@Param("username") String username);

    void updatePassword(@Param("username") String username,@Param("password") String password);
}
