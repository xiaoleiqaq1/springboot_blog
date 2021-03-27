package com.lmk.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.BlogImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
@Mapper
public interface BlogImageDao extends BaseMapper<BlogImage> {
    int addBlogImage(List<BlogImage> blogImageList);

    //blogId为博客id
    List<BlogImage> checkBlogImage(@Param("id") Integer blogId);
}
