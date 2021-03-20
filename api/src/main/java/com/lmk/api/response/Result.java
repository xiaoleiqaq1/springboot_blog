package com.lmk.api.response;

import com.lmk.api.enums.StatusEnum;
import lombok.Data;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/18
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result() {
    }
    public Result(StatusEnum statusEnum) {
        this.code=statusEnum.getCode();
        this.msg=statusEnum.getMsg();
    }

    public Result(StatusEnum statusEnum,T data) {
        this.code=statusEnum.getCode();
        this.msg=statusEnum.getMsg();
        this.data=data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
