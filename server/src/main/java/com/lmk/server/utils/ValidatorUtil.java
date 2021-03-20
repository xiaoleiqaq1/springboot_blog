package com.lmk.server.utils;

import org.springframework.validation.BindingResult;

/**
 * @auth: lmk
 * @Description:对数据进行校验，插件
 * @date: 2021/3/18
 */
public class ValidatorUtil {
    //统一处理校验的结果
    public static String checkResult(BindingResult result) {
        StringBuilder sb = new StringBuilder("");

        if (result != null && result.hasErrors()) {
            /*List<ObjectError> errors=result.getAllErrors();
            for (ObjectError error:errors){
                sb.append(error.getDefaultMessage()).append("\n");
            }*/

            //java8 stream写法
            result.getAllErrors().stream().forEach(error -> sb.append(error.getDefaultMessage()).append(","));
        }

        return sb.toString();
    }
}
