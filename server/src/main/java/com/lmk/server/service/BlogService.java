package com.lmk.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmk.api.utils.PageUtil;
import com.lmk.model.entity.Blog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
public interface BlogService extends IService<Blog> {
    void addBlog(Blog blog, HttpServletRequest request);

    PageUtil checkAllBlog(Map<String,String> paramMap);

    List<Blog> ListAllPraiseBlog(Map<String,String> paramMap);
}
