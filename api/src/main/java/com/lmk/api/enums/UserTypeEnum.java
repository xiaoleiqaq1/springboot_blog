package com.lmk.api.enums;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
public enum  UserTypeEnum {
    ADMIN(1),
    GENERAL(0);

    private Integer code;

    UserTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
