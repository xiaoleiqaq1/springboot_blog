package com.lmk.api.enums;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
public enum  UserStatusEnum {
    ACTIVE(1),
    STOP(0)
    ;
    private Integer code;

    UserStatusEnum() {
    }

    UserStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }}
