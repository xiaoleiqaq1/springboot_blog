package com.lmk.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.model.dao.BlogImageDao;
import com.lmk.model.entity.BlogImage;
import com.lmk.server.service.BlogImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auth: lmk
 * @Description: 添加博客图片
 * @date: 2021/3/24
 */
@Service
public class BlogImageServiceImpl extends ServiceImpl<BlogImageDao, BlogImage> implements BlogImageService {
    private Logger logger= LoggerFactory.getLogger(BlogImageServiceImpl.class);

    @Resource
    private BlogImageDao blogImageDao;

    /*
     * @description 添加博客图片
     * @author lmk
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//事务回滚
    public void addBlogImage(List<BlogImage> blogImageList) {
        try {
            blogImageDao.addBlogImage(blogImageList);
        } catch (Exception e) {
            logger.error("添加图片到数据库异常",e);
        }

    }
}
