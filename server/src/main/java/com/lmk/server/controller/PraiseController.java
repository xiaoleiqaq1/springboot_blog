package com.lmk.server.controller;

import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.Praise;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.model.entity.dto.BlogDto;
import com.lmk.server.service.PraiseService;
import com.lmk.server.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @auth: lmk
 * @Description: 点赞层
 * @date: 2021/3/25
 */
@RestController
@RequestMapping("praise")
public class PraiseController {

    private Logger logger= LoggerFactory.getLogger(PraiseController.class);

    @Resource
    private PraiseService praiseService;

    @RequestMapping(value = "praiseBlog",method = RequestMethod.POST)
    public Result praiseBlog(@Validated Praise praise, BindingResult bindingResult, HttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);

        String res = ValidatorUtil.checkResult(bindingResult);
        if (!StringUtils.isEmpty(res)) {
            return new Result(StatusEnum.PARAMETER_EXCEPTION, res);
        }
        try {
            HttpSession session = request.getSession();
//            System.out.println("这是praise中的session"+session);
            SysUserEntity loginUser =(SysUserEntity) session.getAttribute(RedisConstant.SESSION_KEY);
            //获得sysUserEntity对象中的id
            praise.setUserId(Integer.valueOf(loginUser.getUserId().toString()));

            praiseService.praiseBlog(praise);

        } catch (Exception e) {
            logger.error("点赞博客错误", e);
            result=new Result(StatusEnum.FAIL);
        }
        return result;
    }

    /*
     * @description 点赞博客排行榜
     * @author lmk
     * @date 2021/3/26
     */
    @RequestMapping(value = "praiseBlogTop",method = RequestMethod.GET)
    public Result praiseBlogTop() {
        Result result = new Result(StatusEnum.SUCCESS);
        try {
            List<BlogDto> blogList = praiseService.praiseBlogTop();
            result.setData(blogList);
        } catch (Exception e) {
            logger.error("获取点赞排名博客错误", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }
}
