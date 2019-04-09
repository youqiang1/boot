package com.yq.jpa.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <p> spring MVC web 配置</p>
 * @author yq  2018/3/12 16:36
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * <p> 注册过滤器</p>
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @author yq  2019/4/2 14:28
     */
    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //添加过滤器
        registration.setFilter(new ImCorsFilter());
        //设置过滤路径，/*所有路径
        registration.addUrlPatterns("/*");
        //设置优先级
        registration.setName("imCorsFilter");
        //设置优先级
        registration.setOrder(1);
        return registration;
    }

}
