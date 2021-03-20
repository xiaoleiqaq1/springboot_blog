package com.lmk.server.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @auth: lmk
 * @Description: 丝袜哥
 * @date: 2021/3/17
 */
@Configuration
//开启Swagger功能
@EnableSwagger2
public class Swagger2Config {

    //    Swagger2常用注解说明
//    @Api() 用于类；表示标识这个类是swagger的资源
//    tags–表示说明
//    value–也是说明，可以使用tags替代
//
//    @ApiOperation() 用于方法；表示一个http请求的操作
//    value用于方法描述
//    notes用于提示内容
//
//    @ApiParam() 用于方法，参数，字段说明；表示对参数的添加元数据（说明或是否必填等）
//    name–参数名
//    value–参数说明
//    required–是否必填
//
//    @ApiModel()用于类 ；表示对类进行说明，用于参数用实体类接收
//    value–表示对象名
//
//    @ApiModelProperty()用于方法，字段； 表示对model属性的说明或者数据操作更改
//    value–字段说明
//    name–重写属性名字
//    dataType–重写属性类型
//    required–是否必填
//    example–举例说明
//    hidden–隐藏
//
//    @ApiImplicitParam() 用于方法
//    表示单独的请求参数
//
//    @ApiImplicitParams() 用于方法，包含多个 @ApiImplicitParam
//    name–参数ming
//    value–参数说明
//    dataType–数据类型
//    paramType–参数类型
//    example–举例说明
//
//    @ApiIgnore
//    作用于方法上，使用这个注解swagger将忽略这个接口


    /**
     * http://localhost:8080/swagger-ui.html
     */

    // 定义分隔符
    private static final String splitor = ";";

    /**
     * 创建API应用
     * api() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //扫描接口的包,支持多包扫描
                .apis(basePackage("com.server.controller" + splitor + "com.model.entity"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api文档的详细信息函数
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台管理")//api标题
                .description("后台管理相关接口描述")//api描述
                .version("1.0")
                .contact("111@qq.com")//本API负责人的联系信息
                .license("版权归xxx所有")
                .build();
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}
