package com.yq.mybatis.multi.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p> 动态处理数据源，根据命名区分</p>
 * @author youq  2019/4/27 17:34
 */
@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DataSourceAspect {

    @Before("@annotation(com.yq.mybatis.multi.config.DataSource)")
    public void before(JoinPoint point) {
        Class<?> className = point.getTarget().getClass();
        String methodName = point.getSignature().getName();
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        DatabaseTypeEnum dataSource = DatabaseContextHolder.DEFAULT_DATASOURCE;
        try {
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = method.getAnnotation(DataSource.class);
                // 取出注解中的数据源名
                dataSource = DatabaseTypeEnum.valueOf(annotation.value());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        DatabaseContextHolder.setDatabaseType(dataSource);
    }

}
