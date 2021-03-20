package com.lmk.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
//系统日志
@Data
@TableName("sys_log")
public class SysLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    //用户名
    private String username;

    //用户操作
    private String operation;
    //请求方法
    private String method;
    //请求参数
    private String params;
    //执行时长(毫秒)
    private Long time;
    //IP地址
    private String ip;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;


    //@TableField：字段属性不为数据库表字段，但又是必须使用的
    @TableField(exist = false)
    private List<Long> roleIdList;

    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private List<Long> postIdList;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String postName;

}
