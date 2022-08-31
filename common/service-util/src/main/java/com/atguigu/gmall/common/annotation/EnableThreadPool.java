package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.threadpool.AppTreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AppTreadPoolAutoConfiguration.class)
public @interface EnableThreadPool {

}
