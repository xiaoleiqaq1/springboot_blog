package com.lmk.server.controller;

import com.lmk.api.enums.StatusEnum;
import com.lmk.api.enums.UserStatusEnum;
import com.lmk.api.response.Result;
import com.lmk.model.entity.BlogImage;
import com.lmk.server.service.BlogImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/25
 */
@RestController
@RequestMapping("blogImage")
public class BlogImageController {

    private Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Resource
    private BlogImageService blogImageService;

    @Value("${img.prefix}")
    private String img;

    //上传博客图片
    @RequestMapping(value = "uploadBlogImg",method = RequestMethod.POST)
    public Result uploadBlogImg(MultipartHttpServletRequest request) {
        Result result = new Result(StatusEnum.SUCCESS);

        //放博客图片的集合
        List<BlogImage> blogImageList = new ArrayList<>();
        //获取前端 多个图片的头
        List<MultipartFile> imgPic = request.getFiles("imgPic");
        for (MultipartFile fileImagePic : imgPic) {
            //获取文件的原始名称(不包含完整路径)，比如：1.jpg,1.png
            String originalFilename = fileImagePic.getOriginalFilename();
            //对文件进行截图操作,从“.”开始截取，取到最后一位
            String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (!(fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif"))) {
                result = new Result(StatusEnum.IMGE_EXCEPTION);
            }

            //对图片路径进行拼接
            String newFileName = System.nanoTime() + "." + fileType;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//            获得的是2021-03-24 \\ 123456456468.jpg，
            String FileName = sdf.format(new Date()) + "\\blog\\" + newFileName;
//            获得的是D:\\img\\2021-03-24 \\ 123456456468.jpg，
            String finallFileName = img + FileName;

            File file = new File(finallFileName);

            //如果不存在，则创建文件夹
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                FileCopyUtils.copy(fileImagePic.getBytes(),file);
            } catch (IOException e) {
                logger.error("图片上传错误！");
                result=new Result(StatusEnum.IMGE_ERROR);
            }

            BlogImage blogImage=new BlogImage();
            blogImage.setBlogId(null);
            blogImage.setName(originalFilename);
            blogImage.setSize(fileImagePic.getSize()+"");
            blogImage.setSuffix("."+fileType);
            blogImage.setFileUrl(FileName);
            // 枚举
            blogImage.setIsActive(UserStatusEnum.ACTIVE.getCode());
            blogImage.setCreateTime(new Date());
            blogImageList.add(blogImage);
        }

        blogImageService.addBlogImage(blogImageList);


        //博客图片id
        StringBuffer blogImageIds = new StringBuffer();
        for (BlogImage blogImage : blogImageList) {
            blogImageIds.append(blogImage.getId() + ",");
        }

        // 1,2,3,---->1,2,3
        result.setData(blogImageIds.substring(0, blogImageIds.length() - 1));

        return result;
    }

}
