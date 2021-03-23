package com.lmk.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.UserStatusEnum;
import com.lmk.api.utils.RandomValidateCodeUtil;
import com.lmk.model.dao.LoginDao;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.exceptions.LoginException;
import com.lmk.server.service.LoginService;
import com.lmk.server.utils.CodeUtil;
import com.lmk.server.utils.ShiroUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @auth: lmk
 * @Description: 逻辑处理层
 * @date: 2021/3/17
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginDao, SysUserEntity> implements LoginService {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @Resource
    private LoginDao loginDao;

    @Value("${spring.mail.username}")
    private String emailUrl;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean selectLoginByUsername(String username, String password, String code) {

        //获取redis中的值
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = RedisConstant.STRING_CODE + code;
        if (redisTemplate.hasKey(key)) {
            String redisKey = (String) valueOperations.get(key);
            if (!redisKey.equals(code)) {
                throw new LoginException("验证码不正确,请你重新刷新一下验证码！");
            }
        } else {
            throw new LoginException("获取验证码错误！");
        }

        //判断账号是否存在
        SysUserEntity login = loginDao.selectLoginByUsername(username);


        //判断账号是否正确
        if (StringUtils.isEmpty(login)) {
            throw new LoginException("您输入的账号错误");
        }

//        判断是否为禁用状态
        if (login.getStatus() == UserStatusEnum.STOP.getCode()) {
            throw new LoginException("您的账号被禁用了");
        }

        //判断密码是否正确
        String salt = login.getSalt();
        String stringPassword = ShiroUtil.sha256(password, salt);

        if (!stringPassword.equals(login.getPassword())) {
            throw new LoginException("您的输入的密码不正确");
        }

        return true;
    }


    @Override
    public Boolean forgetPassword(String username, String emailCode, String password) {
        //验证邮箱验证码
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String email =(String) valueOperations.get(RedisConstant.STRING_FRONTEMAIL+emailCode);
        if (!emailCode.equals(email)){
            throw new LoginException("邮箱验证码不正确");
        }

        SysUserEntity sysUserEntity = loginDao.selectLoginByUsername(username);
        //验证账号是否正确
        if (StringUtils.isEmpty(sysUserEntity)){
            throw new LoginException("您输入的账号不正确");
        }

        String salt = sysUserEntity.getSalt();
        sysUserEntity.setPassword(ShiroUtil.sha256(password, salt));

        try {
            loginDao.updatePassword(username,sysUserEntity.getPassword());
        } catch (Exception e) {
            logger.error("修改密码，系统报异常",e);
            e.printStackTrace();
        }

        //将密码存进去
        return true;
    }

    @Override
    public void addRedisCode(String code) {
        //获取redis中的值
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = RedisConstant.STRING_CODE + code;
        //将数据添加进redis中
        valueOperations.set(key, code);

        //指定30秒过期
        redisTemplate.expire(key, 30, TimeUnit.SECONDS);

    }

    @Override
    public void sendEmail(String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailUrl);//设置发件人Email
        simpleMailMessage.setSubject("密码已经忘记，邮箱进行验证");//设置邮件主题
        simpleMailMessage.setTo(email);//设置收件人email

        String code = CodeUtil.getCode();

        simpleMailMessage.setText(code);

        //存进redis中
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String emailCode = RedisConstant.STRING_FRONTEMAIL + code;
        //设置30分钟过期
//        valueOperations.set(emailCode,code,30,TimeUnit.MINUTES);
        valueOperations.set(emailCode,code);

        mailSender.send(simpleMailMessage);//设置邮件主题内容

    }
}
