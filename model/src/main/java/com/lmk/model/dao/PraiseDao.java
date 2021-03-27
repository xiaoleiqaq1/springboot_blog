package com.lmk.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmk.model.entity.Praise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/25
 */
@Mapper
public interface PraiseDao extends BaseMapper<Praise> {

    void praiseBlog(Praise praise);

    int getByBlogIdAndUserId(@Param("userId")Integer userId, @Param("blogId")Integer blogId);
}
