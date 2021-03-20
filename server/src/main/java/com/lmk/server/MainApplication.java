package com.lmk.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/17
 */
@SpringBootApplication
@EnableWebMvc
@MapperScan(basePackages = "com.lmk.model.dao")
@ServletComponentScan(basePackages = "com.lmk.server.config")//扫描丝袜哥
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }
}
