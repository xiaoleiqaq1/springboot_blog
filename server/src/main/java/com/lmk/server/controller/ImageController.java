package com.lmk.server.controller;

import com.lmk.api.constants.RedisConstant;
import com.lmk.api.enums.StatusEnum;
import com.lmk.api.response.Result;
import com.lmk.model.entity.SysUserEntity;
import com.lmk.server.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auth: lmk
 * @Description: 头像博客图片
 * @date: 2021/3/24
 */

@RestController
@RequestMapping("img")
public class ImageController {

    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Resource
    private SysUserService sysUserService;

    @Value("${img.prefix}")
    private String img;

    //第二种方式
    @Resource
    private Environment environment;

    @RequestMapping(value = "uploadImage", method = RequestMethod.POST)
    public Result uploadImage(MultipartHttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);

        String profixProperty = environment.getProperty("img.prefix");

        //前端获取图片路径,
        MultipartFile imgHead = request.getFile("imgHead");
        //1.jpg
        String originalFilename = imgHead.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        if (!(fileType.equals("jpg") || fileType.equals("png"))) {
            return new Result(StatusEnum.IMGE_EXCEPTION);
        }


        //新文件名称
        //时间戳
        //uuid
        //雪花算法
        String fileName = System.nanoTime() + "." + fileType;


        //文件后缀 2021-03-24 \\ 123456456468.jpg，可以加入模块名
        //每天生成一个文件夹，也可以每月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String path = sdf.format(new Date()) + "\\headImage\\" + fileName;
        String filePath = profixProperty + path;


        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            //mkdis()如果文件相同，则不创建
            file.getParentFile().mkdirs();
        }

        try {
            FileCopyUtils.copy(imgHead.getBytes(), file);

            //修改数据库用户的头像url
            HttpSession session = request.getSession();
            //判断session是否为空
            if (!StringUtils.isEmpty(session)) {
                SysUserEntity loginUser = (SysUserEntity) session.getAttribute(RedisConstant.SESSION_KEY);
                SysUserEntity sysUserEntity = new SysUserEntity();

                sysUserEntity.setUserId(loginUser.getUserId());
                sysUserEntity.setImgUrl(path);

                result.setData(path);
                try {
                    sysUserService.update(sysUserEntity);
                } catch (Exception e) {
                    logger.error("保存图片到数据库异常!", e);
                }

            }
        } catch (IOException e) {
            logger.error("上传图片异常!", e);
            result = new Result(StatusEnum.ERROR_EXCEPTION);
        }
        return result;
    }

    //前端一进来就加载图片
    @RequestMapping(value = "updateImage", method = RequestMethod.POST)
    public Result updateImage(HttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);
        try {
            HttpSession session = request.getSession();
            if (StringUtils.isEmpty(session)) {
                result = new Result(StatusEnum.FAIL);
            }
            SysUserEntity loginUser = (SysUserEntity) session.getAttribute(RedisConstant.SESSION_KEY);

            //一进来就去数据库里面查
            SysUserEntity sysUserEntity = sysUserService.selectImage(loginUser.getUserId());

            result.setData(sysUserEntity);
        } catch (Exception e) {
            logger.error("获取图片失败!!", e);
            result = new Result(StatusEnum.FAIL);
        }
        return result;
    }
}
