package com.lmk.server.schedules;

import com.lmk.server.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auth: lmk
 * @Description: 定时器
 * @date: 2021/3/22
 */
@Component
public class RedisSchedule {

    Logger logger = LoggerFactory.getLogger(RedisSchedule.class);

    @Autowired
    private SysUserService sysUserService;

    //第一步：清空redis中的账户数据
    //第二步：查询数据库，并放入redis中
    //时间配置到凌晨2点执行

    //    在子表达式（分钟）里的“0/15”表示从第0分钟开始，每15分钟
    @Scheduled(cron = "* 30/50 * * * ?")
    public void usernameTask() {
        logger.info("清除redis中账号任务开始：" + new Date());
        sysUserService.checkRedisUserName();
        logger.info("清除redis中账号任务结束：" + new Date());
    }

}
