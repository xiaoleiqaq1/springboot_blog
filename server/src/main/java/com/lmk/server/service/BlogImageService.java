package com.lmk.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmk.model.entity.BlogImage;

import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
public interface BlogImageService extends IService<BlogImage> {
    void addBlogImage(List<BlogImage> blogImageList);

}
