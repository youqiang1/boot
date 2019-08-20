package com.yq.redisop.config;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> redisson 哨兵模式 配置</p>
 * @author youq  2019/4/9 14:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redisson.sentinel")
@ConditionalOnProperty(name = "spring.redisson.type", havingValue = "sentinel", matchIfMissing = true)
public class RedissonSentinelConfig {

    private List<String> sentinelAddresses = new ArrayList<>();

    private String masterName;

    private String passWord;

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
                .setPassword(passWord)
                .setConnectTimeout(1000 * 60 * 3);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return config;
    }
}