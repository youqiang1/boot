package com.yq.netty.server.config;

import com.yq.netty.server.listener.NettyServerListener;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> config</p>
 * @author youq  2019/4/19 14:42
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "netty")
public class NettyServerConfig implements CommandLineRunner {

    /**
     * 服务监听端口
     */
    private int port;

    /**
     * 最大线程数
     */
    private int maxThreads;

    /**
     * 最大数据包长度
     */
    private int maxFrameLength;

    @Autowired
    private NettyServerListener nettyServerListener;

    @Override
    public void run(String... args) throws Exception {
        nettyServerListener.start();
    }

}
