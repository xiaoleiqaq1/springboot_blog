package com.lmk.server.annotation;

import java.lang.annotation.*;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String value() default "";

}