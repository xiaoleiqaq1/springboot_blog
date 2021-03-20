package com.lmk.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmk.model.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/17
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    List<SysUserEntity> findAll();

    int add(SysUserEntity sysUserEntity);

    int delete(Long id);

    int update(SysUserEntity sysUserEntity);

    SysUserEntity getById(@Param("id") Long id);

    List<SysUserEntity> checkUserName(@Param("username") String username);

    List<SysUserEntity> checkUserEmail(@Param("email") String email);

}
