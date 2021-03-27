package com.lmk.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.dto.BlogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
@Mapper
public interface BlogDao extends BaseMapper<Blog> {
    List<Blog> checkAllBlog(IPage<Blog> iPage, @Param("paramMap") Map<String,String> paramMap);

    List<Blog> ListAllPraiseBlog(@Param("paramMap") Map<String,String> paramMap);

    BlogDto praiseBlogTop(@Param("blogId")Integer blogId);

}
