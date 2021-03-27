package com.lmk.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmk.api.constants.Constant;
import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.UserStatusEnum;
import com.lmk.model.dao.BlogDao;
import com.lmk.model.dao.PraiseDao;
import com.lmk.model.entity.Blog;
import com.lmk.model.entity.Praise;
import com.lmk.model.entity.dto.BlogDto;
import com.lmk.model.entity.dto.PraiseDto;
import com.lmk.server.service.PraiseService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @auth: lmk
 * @Description: 点赞的微博
 * @date: 2021/3/25
 */
@Service
public class PraiseServiceImpl extends ServiceImpl<PraiseDao, Praise> implements PraiseService {
    @Resource
    private BlogDao blogDao;

    @Resource
    private PraiseDao praiseDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void praiseBlog(Praise praise) {
        Praise newPraise = new Praise();
        newPraise.setBlogId(praise.getBlogId());
        newPraise.setUserId(praise.getUserId());
        newPraise.setCreateTime(new Date());
        newPraise.setPraiseTime(new Date());
        newPraise.setStatus(UserStatusEnum.ACTIVE.getCode());
        praiseDao.praiseBlog(newPraise);


        //往redis中存数据
        //PraiseDto必须重写Equals和HashCode
        PraiseDto praiseDto = new PraiseDto();
        praiseDto.setBlogId(praise.getBlogId());

        ZSetOperations<String, PraiseDto> zSetOperations = redisTemplate.opsForZSet();
        //查询praise中点赞的数量
        Double score = zSetOperations.score(RedisConstant.ZSET_PRAISE, praiseDto);
        if (score == null) {
            //代表着没有人点赞微博，praise表中没有人点赞
            //第一个参数：key,第二个参数：member(元素),第三个参数：score(分数),
            zSetOperations.add(RedisConstant.ZSET_PRAISE, praiseDto, 1);
        } else {
            //在原来的点赞数量+1
            //zSetOperations.add(RedisConstant.ZSET_PRAISE, praiseDto, score+1);
            zSetOperations.incrementScore(RedisConstant.ZSET_PRAISE, praiseDto, 1);
        }
    }

    @Override
    public List<BlogDto> praiseBlogTop() {

        List<BlogDto> list = new ArrayList<>();

        //方式2：从redis查id,再从数据库查count点赞数
        ZSetOperations<String, PraiseDto> zSetOperations = redisTemplate.opsForZSet();
        //查到两条数据
        Long size = zSetOperations.size(RedisConstant.ZSET_PRAISE);
        
        if (size != null && size > 0) {
            Set<PraiseDto> set = zSetOperations.reverseRange(RedisConstant.ZSET_PRAISE, 0, -1);
            set.stream().forEach(n -> {
                BlogDto blogDto = blogDao.praiseBlogTop(n.getBlogId());
                list.add(blogDto);
            });
        }

        return list;
    }
}
