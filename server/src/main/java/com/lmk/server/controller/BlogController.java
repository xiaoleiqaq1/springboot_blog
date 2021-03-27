package com.lmk.server.controller;

import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.api.utils.PageUtil;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.service.BlogService;
import com.lmk.server.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/24
 */
@RestController
@RequestMapping("blog")
public class BlogController {

    private Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "addBlog", method = RequestMethod.POST)
    public Result addBlog(@RequestBody @Validated Blog blog, BindingResult bindingResult, HttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);
        String resString = ValidatorUtil.checkResult(bindingResult);
        if (!StringUtils.isEmpty(resString)) {
            return new Result(StatusEnum.PARAMETER_EXCEPTION, resString);
        }
        try {
            blogService.addBlog(blog, request);
        } catch (Exception e) {
            logger.error("添加博客错误", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    @RequestMapping(value = "checkAllBlog", method = RequestMethod.GET)
    public Result checkAllBlog(@RequestParam Map<String,String> paramMap,HttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);
        try {
            HttpSession session = request.getSession();
            SysUserEntity loginUser =(SysUserEntity) session.getAttribute(RedisConstant.SESSION_KEY);
            //获得sysUserEntity对象中的id
            if (!StringUtils.isEmpty(loginUser)){
                paramMap.put("userId", loginUser.getUserId().toString());
                PageUtil blogList = blogService.checkAllBlog(paramMap);
                result.setData(blogList);
            }
        } catch (Exception e) {
            logger.error("查询所有博客错误", e);
            result=new Result(StatusEnum.FAIL);
        }
        return result;
    }

    @RequestMapping(value = "praiseBlog", method = RequestMethod.GET)
    public Result ListAllPraiseBlog(@RequestParam Map<String,String> paramMap,HttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);
        try {
            HttpSession session = request.getSession();
            SysUserEntity loginUser =(SysUserEntity) session.getAttribute(RedisConstant.SESSION_KEY);
            //获得sysUserEntity对象中的id
            paramMap.put("userId", loginUser.getUserId().toString());

            List<Blog> blogList = blogService.ListAllPraiseBlog(paramMap);
            result.setData(blogList);
        } catch (Exception e) {
            logger.error("查询所有博客错误", e);
            result=new Result(StatusEnum.FAIL);
        }
        return result;
    }
}
