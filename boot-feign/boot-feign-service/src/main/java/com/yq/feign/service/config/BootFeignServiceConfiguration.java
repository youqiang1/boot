package com.yq.feign.service.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * <p> 配置</p>
 * @author youq  2019/4/12 9:18
 */
public class BootFeignServiceConfiguration implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> enableConfiguration = importingClassMetadata.getAnnotationAttributes(getAnnotation().getName());
        if (enableConfiguration == null) {
            return;
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(BootFeignServiceContext.class);
        BeanDefinition beanDefinition = builder.getBeanDefinition();
        registry.registerBeanDefinition("bootFeignConfiguration", beanDefinition);
    }

    private Class<? extends Annotation> getAnnotation() {
        return EnableBootFeignService.class;
    }

    /**
     * <p> 静态内部类，定义feign Service的上下文</p>
     * @EnableFeignClients 指向feign client所在的包，多个用{}括起来并用逗号分隔
     * @ComponentScan 扫描整个feign-service
     * @author youq  2019/4/12 9:25
     */
    @Configuration
    @EnableFeignClients(basePackages = "com.yq.feign.service.feign")
    @ComponentScan(basePackages = "com.yq.feign.service")
    public static class BootFeignServiceContext {
    }

}
