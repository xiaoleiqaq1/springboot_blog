package com.lmk.server.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmk.api.utils.HttpContextUtils;
import com.lmk.api.utils.IPUtil;
import com.lmk.model.entity.SysLogEntity;
import com.lmk.server.annotation.LogAnnotation;
import com.lmk.server.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.lmk.server.annotation.LogAnnotation)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - start;

        saveLog(point, time);
        return result;
    }

    //保存日志
    private void saveLog(ProceedingJoinPoint point, Long time) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLogEntity logEntity = new SysLogEntity();

        //获取请求操作的描述信息
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        if (logAnnotation != null) {
            logEntity.setOperation(logAnnotation.value());
        }

        //获取操作方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        logEntity.setMethod(new StringBuilder(className).append(".").append(methodName).append("()").toString());

        //获取请求参数
        Object[] args = point.getArgs();
        String params = new ObjectMapper().writeValueAsString(args[0]);
        logEntity.setParams(params);

        //获取ip
        logEntity.setIp(IPUtil.getIpAddr(HttpContextUtils.getHttpServletRequest()));

        //获取剩下的参数
        logEntity.setCreateDate(new Date());

        //TODO
        String userName = "admin";
        logEntity.setUsername(userName);

        //执行时间
        logEntity.setTime(time);



        //不建议存关系型数据库MySql，最好存nosql型
        sysLogService.addLog(logEntity);
    }
}