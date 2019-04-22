package com.yq.netty.client.client;

import com.yq.netty.commons.constants.NettyConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> netty客户端</p>
 * @author youq  2019/4/19 19:31
 */
@Slf4j
public class NettyClient {

    private static int retry = 0;

    /**
     * 初始化Bootstrap实例， 此实例是netty客户端应用开发的入口
     */
    private Bootstrap bootstrap;

    /**
     * 远程端口
     */
    private int port;

    /**
     * 远程服务器url
     */
    private String url;

    public NettyClient(int port, String url) {
        this.port = port;
        this.url = url;
        bootstrap = new Bootstrap();
        EventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
    }

    public void start() {
        log.info("{} -> 启动连接【{}:{}】", this.getClass().getName(), url, port);
        bootstrap.handler(new NettyClientHandler());
        ChannelFuture f = bootstrap.connect(url, port);
        try {
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            retry++;
            if (retry > NettyConstants.MAX_RETRY_TIMES) {
                throw new RuntimeException("调用Wrong");
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                log.info("第{}次尝试....失败", retry);
                start();
            }
        }
    }

}
