package com.yq.mybatis.multi.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 数据源注解</p>
 * @author youq  2019/4/27 17:32
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface DataSource {

    String value() default "db1";

}
