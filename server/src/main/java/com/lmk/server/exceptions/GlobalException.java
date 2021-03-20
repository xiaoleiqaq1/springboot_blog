package com.lmk.server.exceptions;

import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auth: lmk
 * @Description:全局异常捕获
 * @date: 2021/3/17
 */
//ResponseBody+ControllerAdvice=@RestControllerAdvice
@RestControllerAdvice
public class GlobalException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerException(NullPointerException e) {
        //实际开发中，会将错误记录在日志中，每天检测有哪些错误报告，通过邮件发送给你
        Result result=new Result(StatusEnum.ERROR_EXCEPTION);
        logger.error("系统发生空指针异常",e);
        return result;
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result arithmeticException(ArithmeticException e) {
        //实际开发中，会将错误记录在日志中，每天检测有哪些错误报告，通过邮件发送给你
        Result result=new Result(StatusEnum.ERROR_EXCEPTION);
        logger.error("系统发生算术异常",e);
        return result;
    }


    @ExceptionHandler(RuntimeException.class)
    public Result runtimeException(RuntimeException e) {
        Result result=new Result(StatusEnum.ERROR_EXCEPTION);
        logger.error("系统发生运行时异常",e);
        return result;
    }


    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        Result result=new Result(StatusEnum.ERROR_EXCEPTION);
        logger.error("系统发生全局异常",e);
        return result;
    }
}
