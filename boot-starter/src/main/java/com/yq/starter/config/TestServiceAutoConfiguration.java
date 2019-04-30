package com.yq.starter.config;

import com.yq.starter.properties.TestProperties;
import com.yq.starter.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 自动配置，用于读取自定义的配置属性和自动注入TestService的bean</p>
 * @author youq  2019/4/30 13:40
 */
@Configuration
@EnableConfigurationProperties(TestProperties.class)
@ConditionalOnClass(TestService.class)
@ConditionalOnProperty(prefix = "spring.test-starter", value = "enabled", matchIfMissing = true)
public class TestServiceAutoConfiguration {

    @Autowired
    private TestProperties properties;

    /**
     * <p> 容器中没有指定bean时会自动配置PestService</p>
     * @return com.yq.starter.service.TestService
     * @author youq  2019/4/30 13:44
     */
    @Bean
    @ConditionalOnMissingBean(TestService.class)
    public TestService testService() {
        TestService testService = new TestService(properties);
        return testService;
    }

}
