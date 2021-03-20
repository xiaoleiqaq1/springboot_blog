package com.lmk.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmk.model.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/20
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

    int addLog(SysLogEntity sysLogEntity);
}
