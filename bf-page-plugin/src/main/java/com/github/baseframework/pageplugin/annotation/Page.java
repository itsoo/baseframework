package com.github.baseframework.pageplugin.annotation;

import java.lang.annotation.*;

/**
 * 分页注解
 *
 * @author zxy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Page {}
