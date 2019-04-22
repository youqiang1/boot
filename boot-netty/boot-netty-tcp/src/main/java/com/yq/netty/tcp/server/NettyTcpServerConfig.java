package com.yq.netty.tcp.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 服务端启动配置</p>
 * @author youq  2019/4/22 14:53
 */
@Configuration
public class NettyTcpServerConfig {

    @Autowired
    private NettyTcpServer nettyTcpServer;

    @Bean
    public Object serverStart() {
        nettyTcpServer.start();
        return new Object();
    }

}
