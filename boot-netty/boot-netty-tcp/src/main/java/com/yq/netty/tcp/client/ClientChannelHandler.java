package com.yq.netty.tcp.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * <p> channel handler</p>
 * @author youq  2019/4/22 14:49
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    private ClientChannelInitializer clientChannelInitializer;

    @Autowired
    private NettyTcpClient nettyTcpClient;

    /**
     * <p> 从服务端收到的消息</p>
     * @param ctx ChannelHandlerContext
     * @param msg 消息
     * @author youq  2019/4/22 14:52
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("接收服务端消息: 【{}】", msg);
    }

    /**
     * <p> 活跃的，有效的通道，第一次连接成功后被调用的方法</p>
     * @param ctx ChannelHandlerContext
     * @author youq  2019/4/22 14:37
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = getRemoteAddress(ctx);
        log.info("连接服务端【{}:{}】成功", remoteAddress.getHostName(), remoteAddress.getPort());
    }

    /**
     * <p> 不活动的通道，连接丢失后执行的方法，据此实现断线重连</p>
     * @param ctx ChannelHandlerContext
     * @author youq  2019/4/22 14:38
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = getRemoteAddress(ctx);
        String host = remoteAddress.getHostName();
        int port = remoteAddress.getPort();
        String server = host + ":" + port;
        ServerChannelManager.remove(server);
        log.info("重连服务端【{}】", server);
        ctx.channel().eventLoop().schedule(() -> nettyTcpClient.reconnect(), 3, TimeUnit.SECONDS);
        ctx.close();
    }

    /**
     * <p> 心跳机制，超时处理</p>
     * @param ctx ChannelHandlerContext
     * @param evt 事件
     * @author youq  2019/4/22 14:39
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            String socketString = ctx.channel().remoteAddress().toString();
            IdleStateEvent event = (IdleStateEvent) evt;
            log.info("server【{}】 state:【{}】", socketString, event.state());
        }
    }

    /**
     * <p> 获取server对象</p>
     * @param ctx ChannelHandlerContext
     * @return java.lang.String
     * @author youq  2019/4/22 14:10
     */
    private InetSocketAddress getRemoteAddress(ChannelHandlerContext ctx) {
        return (InetSocketAddress) ctx.channel().remoteAddress();
    }

}
