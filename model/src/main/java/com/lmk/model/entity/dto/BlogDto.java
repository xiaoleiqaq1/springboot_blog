package com.lmk.model.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/27
 */
@Data
@EqualsAndHashCode
public class BlogDto implements Serializable {
    //新增字段
    //点赞数量
    private Integer count;

    @TableId
    private Integer id;

    @NotBlank(message = "博客内容不能为空")
    private String content;

    private Long userId;

    private Integer type;

    private Integer blogId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer isActive;

    private Integer isDelete;

    private Date deleteTime;

    private Date updateTime;

    private String memoA;

    @TableField(exist = false)
    private String userName;

    //新增字段，获取用户昵称
    @TableField(exist = false)
    private String nickName;

    //新增字段，获取用户头像
    @TableField(exist = false)
    private String headerImg;

    //新增字段，获取博客图片的路径，一对多关系
    @TableField(exist = false)
    private List<String> imgUrls;

    //转发的微博的存储
    @TableField(exist = false)
    private Map<String, Object> otherMap;

    //后端管理微博列表时需要使用到
    @TableField(exist = false)
    private Integer commentTotal = 0;

    @TableField(exist = false)
    private Integer praiseTotal = 0;

    @TableField(exist = false)
    private Integer collectTotal = 0;

    //临时高亮显示的内容
    @TableField(exist = false)
    private String highContent;

    @TableField(exist = false)
    private String blogImageIds;

}
