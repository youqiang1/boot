package com.yq.limit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.Serializable;

/**
 * <p> 限流配置</p>
 * @author youq  2019/4/28 15:16
 */
@Configuration
public class LimitConfig {

    /**
     * <p> 读取限流脚本</p>
     * @return DefaultRedisScript<Number>
     * @author youq  2019/4/28 15:17
     */
    @Bean
    public DefaultRedisScript<Number> redisLuaScript() {
        DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimit.lua")));
        redisScript.setResultType(Number.class);
        return redisScript;
    }

    /**
     * <p> RedisTemplate</p>
     * @param redisConnectionFactory LettuceConnectionFactory
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String, java.io.Serializable>
     * @author youq  2019/4/28 15:23
     */
    @Bean
    public RedisTemplate<String, Serializable> limitRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
