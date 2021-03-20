package com.lmk.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.api.utils.PageUtil;
import com.lmk.api.utils.QueryUtil;
import com.lmk.model.dao.SysLogDao;
import com.lmk.model.entity.SysLogEntity;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @auth: lmk
 * @Description: 逻辑处理层
 * @date: 2021/3/17
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @Resource
    private SysLogDao sysLogDao;

    /*
     * @description 查询所有的日志信息
     * @author lmk
     * @date 2021/3/17
     */
    @Override
    public PageUtil selectLog(Map<String, Object> LogMap) {
        //查询字段
        String searchLog = LogMap.get("username") == null ? "" : (String) LogMap.get("username");
        //传入查询条件
        IPage<SysLogEntity> logPage = new QueryUtil<SysLogEntity>().getQueryPage(LogMap);

        //生成查询条件
        QueryWrapper queryWrapper = new QueryWrapper<SysLogEntity>()
                .like(StringUtils.isNotBlank(searchLog), "username", searchLog.trim());
        //可以多重查询，.like();后面可以继续拼接
        //this.page里面封装了内置的select,update,insert,delete等SQl语句
        IPage<SysLogEntity> page = this.page(logPage, queryWrapper);
        PageUtil pageUtil = new PageUtil(page);
        return pageUtil;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addLog(SysLogEntity sysLogEntity) {
        //判断传进来的对象是否为空
        if (org.springframework.util.StringUtils.isEmpty(sysLogEntity)){
            logger.error("系统日志对象为空了");
            return 500;
        }
        return sysLogDao.addLog(sysLogEntity);
    }
}
