package com.lmk.api.enums;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/18
 */

public enum  StatusEnum {
    SUCCESS(200, "成功"),
    FAIL(-1, "失败"),
    ERROR_EXCEPTION(500,"全局异常信息errorException,请联系管理员"),
    PARAMETER_EXCEPTION(300,"参数异常"),
    LOGIN_EXCEPTION(520, "登录失败"),
    IMGE_EXCEPTION(530, "你上传的不是图片"),
    IMGE_ERROR(540,"你上传图片失败了!")

    ;

    private Integer code;
    private String msg;

    StatusEnum() {
    }

    StatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }}
