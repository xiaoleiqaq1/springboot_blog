package com.lmk.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.UserStatusEnum;
import com.lmk.api.enums.UserTypeEnum;
import com.lmk.api.utils.PageUtil;
import com.lmk.api.utils.QueryUtil;
import com.lmk.model.dao.SysUserDao;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.exceptions.GlobalException;
import com.lmk.server.service.SysUserService;
import com.lmk.server.utils.ShiroUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auth: lmk
 * @Description: 逻辑处理层
 * @date: 2021/3/17
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @Resource
    private SysUserDao userDao;

    @Resource
    private RedisTemplate redisTemplate;

    /*
     * @description 查询所有
     * @author lmk
     * @date 2021/3/17
     * @param sysUserEntity
     * @return int
     */
    @Override
    public PageUtil findAll(Map<String, Object> map) {
        //查询字段
        String search = map.get("username") == null ? "" : (String) map.get("username");

        //传入查询条件
        IPage<SysUserEntity> queryPage = new QueryUtil<SysUserEntity>().getQueryPage(map);

        //生成查询条件
        QueryWrapper queryWrapper = new QueryWrapper<SysUserEntity>()
                .like(StringUtils.isNotBlank(search), "username", search.trim());
//                .like();可以继续拼接

        //this.page里面封装了内置的select,update,insert,delete等SQl语句
        IPage<SysUserEntity> page = this.page(queryPage, queryWrapper);

        PageUtil pageUtil = new PageUtil(page);

        return pageUtil;
    }

    /*
     * @description 添加操作
     * @author lmk
     * @date 2021/3/17
     * @param sysUserEntity
     * @return int
     */
    //默认密码+盐
    @Value("user.default.password")
    private String defaultPassword;

    @Override
    @Transactional(rollbackFor = Exception.class)//事务回滚
    public int add(SysUserEntity sysUserEntity) {

        //深拷贝  浅拷贝
        //SysUserEntity user = sysUserEntity;
        SysUserEntity user = new SysUserEntity();
        //org.springframework.beans.BeanUtils
        //深拷贝工具
        BeanUtils.copyProperties(sysUserEntity, user);

        //设置默认密码
        //加密密码串
        //需要导入这个包：org.apache.commons.lang3.RandomStringUtils
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(ShiroUtil.sha256(defaultPassword, salt));
        user.setSalt(salt);
        user.setStatus(UserStatusEnum.ACTIVE.getCode());
        user.setType(UserTypeEnum.ADMIN.getCode());
        user.setCreateTime(new Date());
        //TODO
        user.setImgUrl("");

        int result = userDao.add(user);
        if (result > 0) {
            //只有添加成功后，才把数据存入到redis中
            //redis不支持事务
            addRedisUserName2(user.getUsername());
            addRedisUserEmail2(user.getEmail());
        }

        return result;
    }


    public void addRedisUserName2(String username){
        SetOperations setOperations = redisTemplate.opsForSet();
        //将数据添加进redis中
        setOperations.add(RedisConstant.SET_USERNAME,username);
    }

    public void addRedisUserEmail2(String email){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(RedisConstant.SET_EMAIL,email);
    }


    /*
     * @description 检查用户名username是否为空
     * @author lmk
     * @date 2021/3/20
     */
    @Override
    public Boolean addRedisUserName(String username){
        SetOperations setOperations = redisTemplate.opsForSet();
        //redis自带的添加key，value方法
        //相当于包含
        Boolean member = setOperations.isMember(RedisConstant.SET_USERNAME, username);
        //member返回true,相当于redis缓存中已经存在了username
        if (member==true){
            //直接返回false,直接从redis中获取
            return false;
        }else{
            //member等于flase,相当于redis中没有数据，则需要从数据库中查找
            //必须用list集合去接收，如果存在相同的账号，则会有两个select语句，两条数据返回
            List<SysUserEntity> checkName = userDao.checkUserName(username);
            //查询不到值，账号不存在，可以添加
            if (checkName == null || checkName.size() == 0) {
                return true;
            } else {
                //已经存在账号，两条数据返回
                if (checkName.size() > 1) {
                    //数据库中已经存在了相同的账号
                    logger.error("数据库中已经存在了相同的账号");
                }else {
                    //只有一条数据
                    //将数据添加进redis中
                    setOperations.add(RedisConstant.SET_USERNAME,username);
                }
                return false;
            }
        }
    }

    /*
     * @description 检查邮箱email是否为空
     * @author lmk
     * @date 2021/3/20
     */
    @Override
    public Boolean addRedisUserEmail(String email) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(RedisConstant.SET_EMAIL);
        if (members != null) {
            for (String str : members) {
                if (email.equals(str)) {
                    //从redis中查到相同的账号，直接返回false
                    return false;
                }
            }
        }

        //如果redis中没有，则查数据库，查不到则可以注册，查到了放入redis
        List<SysUserEntity> userList = userDao.checkUserEmail(email);
        if (userList == null || userList.size() == 0) {
            return true;
        } else {
            if (userList.size() > 1) {
                //数据库有相同的账号
                logger.error("数据库有相同的邮箱email");
            }
            setOperations.add(RedisConstant.SET_EMAIL, email);
            return false;
        }
    }


    /*
     * @description 删除
     * @author lmk
     * @date 2021/3/17
     * @param id
     * @return int
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)//事务回滚
//    public int delete2(Long id) {
//        return userDao.delete(id);
//    }

    //mybatis-plus中的删除
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return this.removeById(id);
    }


    /*
     * @description 修改
     * @author lmk
     */
    @Override
    @Transactional(rollbackFor = Exception.class)//事务回滚
    public int update(SysUserEntity sysUserEntity) {
        return userDao.update(sysUserEntity);
    }

    @Override
    public SysUserEntity getById(Long id) {
        return userDao.getById(id);
    }

//    @Override
    //第一种方式，没有缓存redis中的检查用户名
    public Boolean checkUserName(String username) {
        //必须用list集合去接收，如果存在相同的账号，则会有两个select语句，两条数据返回
        List<SysUserEntity> checkName = userDao.checkUserName(username);
        //查询不到值，账号不存在
        if (checkName == null || checkName.size() == 0) {
            return true;
        } else {
            //已经存在账号，两条数据返回
            if (checkName.size() > 1) {
                //数据库中已经存在了相同的账号
                logger.error("数据库中已经存在了相同的账号");
            }
            return false;
        }
    }
}
