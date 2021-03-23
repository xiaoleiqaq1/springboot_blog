package com.lmk.server.controller;


import com.google.code.kaptcha.Producer;
import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.exceptions.LoginException;
import com.lmk.server.service.LoginService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @auth: lmk
 * @Description: 控制台，连接前端
 * @date: 2021/3/17
 */
@Api("用户登录api")
@Controller
@RequestMapping("loginUser")
public class LoginController {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @Resource
    private LoginService loginService;

    @Autowired
    private Producer producer;

    //查询所有
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result selectLoginByUsername(String username, String password, String code, HttpServletRequest request) {

        Result result = new Result(StatusEnum.SUCCESS);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            result = new Result(StatusEnum.FAIL);
        }

        try {
            boolean b = loginService.selectLoginByUsername(username, password, code);

            HttpSession session = request.getSession();
            session.setAttribute(RedisConstant.SESSION_KEY, b);
//            System.out.println("这是login获取的:"+session);
            if (b != true) {
                result = new Result(StatusEnum.FAIL);
            }
        } catch (LoginException e) {
            result = new Result(StatusEnum.LOGIN_EXCEPTION);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("添加用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }

        return result;
    }

    //忘记密码
    @RequestMapping(value = "forget", method = RequestMethod.POST)
    @ResponseBody
    public Result forgetPassword(String username, String emailCode, String password) {

        Result result = new Result(StatusEnum.SUCCESS);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(emailCode) || StringUtils.isEmpty(password)) {
            result = new Result(StatusEnum.FAIL);
        }
        try {
            Boolean b = loginService.forgetPassword(username, emailCode, password);
            if (b != true) {
                logger.error("修改密码报异常");
                result = new Result(StatusEnum.FAIL);
            }
        } catch (LoginException e) {
            result = new Result(StatusEnum.LOGIN_EXCEPTION);
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("添加用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }

    //获取验证码
    @RequestMapping(value = "email", method = RequestMethod.POST)
    @ResponseBody
    public Result sendEmail(String email) {

        Result result = new Result(StatusEnum.SUCCESS);

        if (StringUtils.isEmpty(email)) {
            result = new Result(StatusEnum.FAIL);
        }
        try {
            loginService.sendEmail(email);

        } catch (Exception e) {
            logger.error("添加用户发生了异常", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }


    //查询所有
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        //将生成的验证码传递过去
        loginService.addRedisCode(text);

        //输出流
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
