package com.lmk.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.Praise;
import com.lmk.model.entity.dto.BlogDto;

import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/25
 */
public interface PraiseService extends IService<Praise> {
    void praiseBlog(Praise praise);

    List<BlogDto> praiseBlogTop();
}
