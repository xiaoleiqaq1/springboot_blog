package com.lmk.server.controller;


import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.api.utils.PageUtil;
import com.lmk.api.utils.RandomValidateCodeUtil;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.annotation.LogAnnotation;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.service.SysUserService;
import com.lmk.server.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @auth: lmk
 * @Description: 控制台，连接前端
 * @date: 2021/3/17
 */
@Api("用户管理api")
@RestController
@RequestMapping("sysUser")
public class SysUserController {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @Resource
    private SysUserService sysUserService;

    //查询所有
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public Result findAll(@RequestParam Map<String, Object> map) {

        Result result = new Result(StatusEnum.SUCCESS);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            PageUtil page = sysUserService.findAll(map);
            resultMap.put("page", page);
            result.setData(resultMap);
        } catch (Exception e) {
            logger.error("查询用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    //    添加操作
    @LogAnnotation("添加用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(@RequestBody @Validated SysUserEntity sysUserEntity, BindingResult bindingResult) {
        Result result = new Result(StatusEnum.SUCCESS);
        String validator = ValidatorUtil.checkResult(bindingResult);

        if (!StringUtils.isEmpty(validator)) {
            return new Result(StatusEnum.PARAMETER_EXCEPTION, validator);
        }

        try {
            int addResult = sysUserService.add(sysUserEntity);
            if (addResult <= 0) {
                result = new Result(StatusEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("添加用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    //   验证用户名
    @RequestMapping(value = "checkUserName", method = RequestMethod.GET)
    public Result checkUserName(String username) {
        Result result = new Result(StatusEnum.SUCCESS);
        //检验用户名是否为空，如果为空直接返回fail
        if (StringUtils.isEmpty(username)) {
            return new Result(StatusEnum.FAIL);
        }
        Boolean b = sysUserService.addRedisUserName(username);
        if (b != true) {
            //查询不到值，账号不存在，可以添加
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    //   验证邮箱
    @RequestMapping(value = "checkUserEmail", method = RequestMethod.GET)
    public Result checkUserEmail(String email) {
        Result result = new Result(StatusEnum.SUCCESS);
        //检验用户名是否为空，如果为空直接返回fail
        if (StringUtils.isEmpty(email)) {
            return new Result(StatusEnum.FAIL);
        }
        if (!sysUserService.addRedisUserEmail(email)) {
            //查询不到值，账号不存在，可以添加
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    //    删除操作
    @LogAnnotation("删除用户")
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public Result delete(Long id) {
        Result result = new Result(StatusEnum.SUCCESS);
        if (StringUtils.isEmpty(id)) {
            return new Result(StatusEnum.FAIL);
        }
        try {
            //mybatis-plus自带的删除语句
            boolean b = sysUserService.removeById(id);
            if (!b) {
                result = new Result(StatusEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("删除用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    //   根据id查询
    @RequestMapping(value = "getById", method = RequestMethod.GET)
    public Result getById(Long id) {
        Result result = new Result(StatusEnum.SUCCESS);
        if (StringUtils.isEmpty(id)) {
            result = new Result(StatusEnum.PARAMETER_EXCEPTION);
            return result;
        }

        try {
            SysUserEntity sysUser = sysUserService.getById(id);
            if (sysUser == null) {
                result = new Result(StatusEnum.FAIL);
            }
            result.setData(sysUser);
        } catch (Exception e) {
            logger.error("根据id查询发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }


    //   修改操作
    @LogAnnotation("修改用户")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody @Validated SysUserEntity sysUserEntity, BindingResult bindingResult) {
        Result result = new Result(StatusEnum.SUCCESS);

        String validator = ValidatorUtil.checkResult(bindingResult);
        if (!StringUtils.isEmpty(validator)) {
            return new Result(StatusEnum.PARAMETER_EXCEPTION, validator);
        }
        try {
            int update = sysUserService.update(sysUserEntity);
            if (update <= 0) {
                result = new Result(StatusEnum.FAIL);
            }
        } catch (Exception e) {
            logger.error("修改用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>> ", e);
        }
    }


    /**
     * 忘记密码页面校验验证码
     */
    @RequestMapping(value = "/checkVerify", method = RequestMethod.POST, headers = "Accept=application/json")
    public boolean checkVerify(@RequestBody Map<String, Object> requestMap, HttpSession session) {
        try {
            //从session中获取随机数
            String inputStr = requestMap.get("inputStr").toString();
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return false;
            }
            if (random.equals(inputStr)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("验证码校验失败", e);
            return false;
        }
    }
}
