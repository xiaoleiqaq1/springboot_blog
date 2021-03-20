package com.lmk.server.controller;


import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.api.utils.PageUtil;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.service.SysLogService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @auth: lmk
 * @Description: 控制台，连接前端
 * @date: 2021/3/17
 */
@Api("日志管理api")
@RestController
@RequestMapping("sysLog")
public class SysLogController {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @Resource
    private SysLogService sysLogService;

    //查询所有
    @RequestMapping(value = "selectLog", method = RequestMethod.GET)
    public Result selectLog(@RequestParam Map<String,Object> map) {
        Result result = new Result(StatusEnum.SUCCESS);
        Map<String,Object> resultMap=new HashMap<>();
        try {
            PageUtil page = sysLogService.selectLog(map);
            resultMap.put("page",page);
            result.setData(resultMap);
        } catch (Exception e) {
            logger.error("查询用户发生了异常",e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }



}
