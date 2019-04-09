package com.yq.redisop.config;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Data;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> redisson 集群配置</p>
 * @author youq  2019/4/9 14:18
 */
@Data
@Configuration
@Conditional(RedissonClusterCondition.class)
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonClusterConfig {

    private LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    /**
     * 每个节点的最小空闲连接量
     */
    private int slaveConnectionMinimumIdleSize = 32;

    /**
     * 每个节点的最大连接池大小
     */
    private int slaveConnectionPoolSize = 64;

    private int failedSlaveReconnectionInterval = 3000;

    private int failedSlaveCheckInterval = 60000;

    /**
     * Redis 'master' node minimum idle connection amount for <b>each</b> slave node
     */
    private int masterConnectionMinimumIdleSize = 32;

    /**
     * Redis 'master' node maximum connection pool size
     */
    private int masterConnectionPoolSize = 64;

    private ReadMode readMode = ReadMode.SLAVE;

    private SubscriptionMode subscriptionMode = SubscriptionMode.MASTER;

    /**
     * Redis 'slave' node minimum idle subscription (pub/sub) connection amount for <b>each</b> slave node
     */
    private int subscriptionConnectionMinimumIdleSize = 1;

    /**
     * Redis 'slave' node maximum subscription (pub/sub) connection pool size for <b>each</b> slave node
     */
    private int subscriptionConnectionPoolSize = 50;

    private long dnsMonitoringInterval = 5000;

    /**
     * Redis cluster node urls list
     */
    private List<String> nodeAddresses = new ArrayList<>();

    /**
     * Redis cluster scan interval in milliseconds
     */
    private int scanInterval = 1000;

    private String codec = "org.redisson.codec.JsonJacksonCodec";

    @Bean
    Config config() throws Exception {
        final int size = nodeAddresses.size();
        if (nodeAddresses.isEmpty()) {
            throw new RuntimeException("redisson node address is empty");
        }
        String[] nodes = nodeAddresses.toArray(new String[size]);
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress(nodes)
                .setLoadBalancer(getLoadBalancer())
                .setMasterConnectionPoolSize(getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(getSlaveConnectionPoolSize())
                .setSubscriptionConnectionPoolSize(getSubscriptionConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(getMasterConnectionMinimumIdleSize())
                .setSlaveConnectionMinimumIdleSize(getSlaveConnectionMinimumIdleSize())
                .setSubscriptionConnectionMinimumIdleSize(getSubscriptionConnectionMinimumIdleSize())
                .setReadMode(getReadMode())
                .setSubscriptionMode(getSubscriptionMode())
                .setDnsMonitoringInterval(getDnsMonitoringInterval())
                .setFailedSlaveCheckInterval(getFailedSlaveCheckInterval())
                .setFailedSlaveReconnectionInterval(getFailedSlaveReconnectionInterval());
        Codec codec = (Codec) ClassUtils.forName(getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return config;
    }

}
