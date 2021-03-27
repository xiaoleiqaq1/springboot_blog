package com.lmk.server.controller;


import com.google.code.kaptcha.Producer;
import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.exceptions.LoginException;
import com.lmk.server.service.EmailService;
import com.lmk.server.service.LoginService;
import com.lmk.server.utils.CodeUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
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

    @Value("${spring.mail.username}")
    private String emailUrl;

    @Autowired
    private Producer producer;

    @Resource
    private EmailService emailService;

    @Autowired
    private RedisTemplate redisTemplate;

    //查询所有
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result selectLoginByUsername(String username, String password, String code, HttpServletRequest request) {

        Result result = new Result(StatusEnum.SUCCESS);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            result = new Result(StatusEnum.FAIL);
        }

        try {
            //设置session
            boolean b = loginService.selectLoginByUsername(username, password, code,request);


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
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailUrl);
            message.setTo(email);
            message.setSubject("忘记密码，邮箱验证");
            //随机验证码
            String code = CodeUtil.getCode();
            message.setText(code);
            //存进redis中
            ValueOperations valueOperations = redisTemplate.opsForValue();
            String emailCode = RedisConstant.STRING_FRONTEMAIL + code;
            //设置30分钟过期
//        valueOperations.set(emailCode,code,30,TimeUnit.MINUTES);
            valueOperations.set(emailCode,code);

            emailService.sendEmail(message);

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
