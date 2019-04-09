package com.yq.redisop.config;

import com.yq.redisop.common.RedissonTypeEnum;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p> redisson 集群的判断条件</p>
 * @author youq  2019/4/9 14:12
 */
@Configuration
public class RedissonClusterCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String redissonType = context.getEnvironment().getProperty("spring.redisson.type");
        if (RedissonTypeEnum.CLUSTER.name().equals(redissonType.trim())) {
            return true;
        } else if (!RedissonTypeEnum.SENTINEL.name().equals(redissonType.trim())
                && !RedissonTypeEnum.SINGLE.name().equals(redissonType.trim())) {
            throw new RuntimeException("The value of 'spring.redisson.type' is not 'single' or 'cluster' or 'sentinel'.");
        }
        return false;
    }

}
