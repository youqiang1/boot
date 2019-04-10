package com.yq.redisop;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 使用注解</p>
 * //@Target 指明注解应用于类、接口或枚举声明；
 * //@Retention 指明注解应用于运行时。
 * @author youq  2019/4/9 14:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisOperateContext.class)
public @interface EnableRedisOperate {
}
