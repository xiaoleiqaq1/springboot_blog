package com.lmk.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class BlogImage implements Serializable {

    @TableId
    private Integer id;

    private Integer blogId;

    private String name;

    private String size;

    private String suffix;

    private String fileUrl;

    private Integer isActive = 1;

    private Date createTime;

}
