package com.yq.netty.client.config;

import com.yq.netty.client.util.NettyBeanScanner;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> netty客户端配置</p>
 * @author youq  2019/4/19 19:39
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

    private String url;

    private int port;

    /**
     * 初始化加载Netty相关bean的配置方法
     * @param basePackage 配置的包名
     * @param clientName  配置的Netty实例对象名
     * @return NettyBeanScanner
     * @author youq  2019/4/19 19:55
     */
    @Bean
    public static NettyBeanScanner initNettyBeanScanner(@Value("${netty.basePackage}") String basePackage,
                                                        @Value("${netty.clientName}") String clientName) {
        // 创建对象
        return new NettyBeanScanner(basePackage, clientName);
    }

}
