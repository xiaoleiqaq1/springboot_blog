package com.lmk.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/18
 */
@Configuration//解决跨域问题
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${img.prefix}")
    private String imgPrefix;

    //对丝袜哥放行
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 图片上传与访问配置
        //前端访问方式 :域名//上下文//image//真实存储路径
        //例如：http://localhost:8090//image//20200101//sys//1.png
        // ResourceHandler:前台访问的目录
        // ResourceLocations为图片相对应的本地路径
        //registry.addResourceHandler("/image/**").addResourceLocations("file:d:/img/");
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + imgPrefix);

        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        //addMapping 指定服务器上哪些资源可以被访问， /**设置所有的请求可以进行跨域
        registry.addMapping("/**")
                //允许跨域的ip，例如：allowedOrigins("http://localhost:8080")
                .allowedOrigins("*")
                .allowCredentials(true)
                //请求的方法
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3600);
    }
}