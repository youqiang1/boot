package com.yq.limit.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 限流注解</p>
 * @author youq  2019/4/28 15:22
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流唯一标识
     */
    String key() default "";

    /**
     * 限流时间
     */
    int time();

    /**
     * 限流次数
     */
    int count();

}
