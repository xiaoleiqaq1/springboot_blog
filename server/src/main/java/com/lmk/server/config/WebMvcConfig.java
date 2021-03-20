package com.lmk.server.config;

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

    //对丝袜哥放行
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
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