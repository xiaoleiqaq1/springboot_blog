package com.lmk.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmk.api.utils.PageUtil;
import com.lmk.model.entity.SysLogEntity;

import java.util.Map;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/20
 */
public interface SysLogService extends IService<SysLogEntity> {
    PageUtil selectLog(Map<String,Object> map);

    int addLog(SysLogEntity sysLogEntity);
}
