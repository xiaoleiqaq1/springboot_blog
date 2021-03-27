package com.lmk.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.api.constants.Constant;
import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.UserStatusEnum;
import com.lmk.api.utils.PageUtil;
import com.lmk.api.utils.QueryUtil;
import com.lmk.model.dao.BlogDao;
import com.lmk.model.dao.BlogImageDao;
import com.lmk.model.dao.PraiseDao;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.BlogImage;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.service.BlogImageService;
import com.lmk.server.service.BlogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @auth: lmk
 * @Description: 博客server层
 * @date: 2021/3/24
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements BlogService {
    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogImageService blogImageService;

    @Resource
    private PraiseDao praiseDao;

    @Resource
    private BlogDao blogDao;

    @Resource
    private BlogImageDao blogImageDao;

    @Value("${server.port}")
    private String port;

    /*
     * @description 添加个人博客，发布博客图片和内容
     * @author lmk
     * @date 2021/3/25
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBlog(Blog blog, HttpServletRequest request) {
        Blog newBlog = new Blog();
        newBlog.setContent(blog.getContent());
        HttpSession session = request.getSession();

        SysUserEntity loginUser = (SysUserEntity) session.getAttribute(RedisConstant.SESSION_KEY);

        newBlog.setUserId(loginUser.getUserId());
        newBlog.setCreateTime(new Date());
        //TODO 枚举
        newBlog.setType(UserStatusEnum.ACTIVE.getCode());
        newBlog.setIsActive(UserStatusEnum.ACTIVE.getCode());

        //添加博客
        this.save(newBlog);

        //修改博客图片中的blog_id
        // 1,2,3
        String blogImageIds = blog.getBlogImageIds();
        if (StringUtils.isNotBlank(blogImageIds)) {

            String[] split = blogImageIds.split(",");
            List<BlogImage> blogImageList = new ArrayList<>();
            for (String blogImageId : split) {
                BlogImage blogImage = new BlogImage();
                blogImage.setId(Integer.valueOf(blogImageId));
                blogImage.setBlogId(newBlog.getId());
                blogImageList.add(blogImage);
            }
            blogImageService.updateBatchById(blogImageList);
        }

    }

    /*
     * @description 查询所有博客信息
     * @author lmk
     */
    @Override
    public PageUtil checkAllBlog(Map<String, String> paramMap) {
//        构造查询条件
        Map<String, Object> map = new HashMap<>();
        map.put("searchType", paramMap.get("searchType"));
        map.put(Constant.PAGE, paramMap.get("page") == null ? 1 : paramMap.get("page"));
        map.put(Constant.LIMIT, 2);

        //传入查询条件
        IPage<Blog> iPage = new QueryUtil<Blog>().getQueryPage(map);

        List<Blog> blogList = blogDao.checkAllBlog(iPage, paramMap);
        if (CollectionUtils.isNotEmpty(blogList)) {
            // 前缀
            String prefix = "http://localhost:" + port + "/image/";
            //获取博客id
            for (Blog b : blogList) {
                //将博客id放进blog_image表中，获取file_url :2021-03-24\blog\50880442772350.jpg
                List<BlogImage> blogImagesUrl = blogImageDao.checkBlogImage(b.getId());
                if (CollectionUtils.isNotEmpty(blogImagesUrl)) {
                    List<String> blogimgUrls = new ArrayList<>();
                    //将图片遍历出来,用新集合接收
                    for (BlogImage blogImage : blogImagesUrl) {
                        blogimgUrls.add(prefix + blogImage.getFileUrl());
                    }
                    //将博客图片路径重新放进去
                    b.setImgUrls(blogimgUrls);
                    //将headImage头像图片放进去
                    b.setHeaderImg(prefix + b.getHeaderImg());
                }

                //是否：点赞，收藏，转发
                Map<String, Object> otherMap = new HashMap<>();

                String getPraiseString = praiseDao.getByBlogIdAndUserId(Integer.valueOf(paramMap.get("userId")), b.getId()) > 0 ? "已点赞" : "未点赞";
                otherMap.put("isPraise", getPraiseString);
                b.setOtherMap(otherMap);
            }

        }
        iPage.setRecords(blogList);
        return new PageUtil(iPage);

    }

    /*
     * @description 查看所有点赞的微博
     * @author lmk
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Blog> ListAllPraiseBlog(Map<String, String> paramMap) {
        List<Blog> blogList = blogDao.ListAllPraiseBlog(paramMap);

        if (CollectionUtils.isNotEmpty(blogList)) {
            // 前缀
            String prefix = "http://localhost:" + port + "/image/";
            for (Blog b : blogList) {
                //将博客id放进blog_image表中，获取file_url :2021-03-24\blog\50880442772350.jpg
                List<BlogImage> blogImagesUrl = blogImageDao.checkBlogImage(b.getId());
                List<String> blogimgUrls = new ArrayList<>();
                //将图片遍历出来,用新集合接收
                for (BlogImage blogImage : blogImagesUrl) {
                    blogimgUrls.add(prefix + blogImage.getFileUrl());
                }
                //将博客图片路径重新放进去
                b.setImgUrls(blogimgUrls);
                //将headImage头像图片放进去
                b.setHeaderImg(prefix + b.getHeaderImg());
            }
        }

        //往里面赋值
        return blogList;
    }
}
