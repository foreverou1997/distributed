package com.demo.distributed.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author oulei
 * @Description
 * @date 2019/12/4 10:59
 */
//该元注解表示定义的注解只能用于方法上
@Target(value = ElementType.METHOD)
//该元注解表示自定义注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在（用于在运行时动态获取注解信息）
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimiterAnnotation {
    //每秒生成令牌的速率
    double speed();

    //获取令牌的超时时间
    long timeout();
}
