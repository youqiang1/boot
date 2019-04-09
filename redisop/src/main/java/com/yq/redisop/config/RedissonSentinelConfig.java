package com.yq.redisop.config;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> redisson 哨兵模式 配置</p>
 * @author youq  2019/4/9 14:29
 */
@Data
@Configuration
@Conditional(RedissonSentinelCondition.class)
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonSentinelConfig {

    private List<String> sentinelAddresses = new ArrayList<>();

    private String masterName;

    private String password;

    @Bean
    public Config config() throws Exception {
        final int size = sentinelAddresses.size();
        if (sentinelAddresses.isEmpty()) {
            throw new RuntimeException("redisson sentinel node address is empty!");
        }
        String[] nodeArray = new String[size];
        String[] nodes = sentinelAddresses.toArray(nodeArray);
        Config config = new Config();
        config.useSentinelServers()
                .addSentinelAddress(nodes)
                .setMasterName(masterName)
                .setPassword(password)
                .setConnectTimeout(1000 * 60 * 3);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return config;
    }
}