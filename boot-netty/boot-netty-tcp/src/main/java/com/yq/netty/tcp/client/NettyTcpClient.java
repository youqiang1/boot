package com.yq.netty.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p> netty tcp客户端</p>
 * @author youq  2019/4/22 14:46
 */
@Slf4j
@Component
public class NettyTcpClient {

    @Value(("${netty.tcp.server.host}"))
    private String host;

    @Value("${netty.tcp.server.port}")
    private int port;

    public static EventLoopGroup worker = new NioEventLoopGroup();

    @Autowired
    private ClientChannelInitializer clientChannelInitializer;

    /**
     * 与服务端建立连接后得到的通道对象
     */
    private Channel channel;

    /**
     * <p> 初始化Bootstrap客户端引导程序</p>
     * @return io.netty.bootstrap.Bootstrap
     * @author youq  2019/4/22 15:14
     */
    private Bootstrap getBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                //通道连接者
                .channel(NioSocketChannel.class)
                //通道处理者
                .handler(clientChannelInitializer)
                //心跳
                .option(ChannelOption.SO_KEEPALIVE, true);
        return bootstrap;
    }

    /**
     * <p> 建立连接，获取连接通道对象</p>
     * @author youq  2019/4/22 15:15
     */
    public void connect() {
        ChannelFuture future = getBootstrap().connect(host, port).syncUninterruptibly();
        if (future != null && future.isSuccess()) {
            channel = future.channel();
            String key = host + ":" + port;
            ServerChannelManager.add(key, channel);
        } else {
            log.info("连接服务端【{}:{}】失败", host, port);
        }
    }

    /**
     * <p> 断线重连，获取连接通道对象</p>
     * @author youq  2019/4/22 15:15
     */
    public void reconnect() {
        ChannelFuture future = getBootstrap().connect(host, port).syncUninterruptibly();
        if (future != null && future.isSuccess()) {
            channel = future.channel();
            String key = host + ":" + port;
            ServerChannelManager.add(key, channel);
        } else {
            log.info("重连服务端【{}:{}】失败", host, port);
        }
    }

    /**
     * <p> 消息发送</p>
     * @param msg 消息内容
     * @author youq  2019/4/22 15:19
     */
    public void sendMsg(Object msg) throws Exception {
        if (channel != null) {
            channel.writeAndFlush(msg).sync();
        } else {
            log.info("连接未建立，消息发送失败！");
        }
    }

}
