package com.yq.redisop;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p> redis 操作模块 上下文</p>
 * @author youq  2019/4/9 13:56
 */
@Configuration
@ComponentScan(basePackages = "com.yq.redisop")
public class RedisOperateContext {

    @Bean
    public RedissonClient redisson(@Autowired Config config) {
        return Redisson.create(config);
    }

}
