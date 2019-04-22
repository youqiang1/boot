package com.yq.netty.client.client;

import com.yq.netty.client.util.ChannelUtil;
import com.yq.netty.commons.constants.NettyConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
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

    /**
     * <p> 自定义的NettyClientHandler</p>
     * @author youq  2019/4/19 19:41
     */
    @Slf4j
    public static class NettyClientHandlerAdapter extends ChannelHandlerAdapter {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            NettyClientHandlerAdapter.log.error("{} -> [通道异常] {}", this.getClass().getName(), ctx.channel().id());
            ChannelUtil.remove(ctx.channel());
        }

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
            NettyClientHandlerAdapter.log.info("{} -> [连接建立成功] {}", this.getClass().getName(), channelHandlerContext.channel().id());
            // 注册通道
            ChannelUtil.registerChannel(channelHandlerContext.channel());
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!(msg instanceof Exception)) {
                NettyClientHandlerAdapter.log.info("{} -> [客户端收到的消息] {}", this.getClass().getName(), msg);
            }
            String longText = ctx.channel().id().asLongText();
            String resultKey = ChannelUtil.getResultKey(longText);
            ChannelUtil.calculateResult(resultKey, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            NettyClientHandlerAdapter.log.info("{} -> [客户端消息接收完毕] {}", this.getClass().getName(), ctx.channel().id());
            super.channelReadComplete(ctx);
            boolean active = ctx.channel().isActive();
            NettyClientHandlerAdapter.log.info("{} -> [此时通道状态] {}", this.getClass().getName(), active);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            NettyClientHandlerAdapter.log.info("{} -> [客户端心跳监测发送] 通道编号：{}", this.getClass().getName(), ctx.channel().id());
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush("ping-pong-ping-pong");
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            NettyClientHandlerAdapter.log.info("{} -> [关闭通道] {}", this.getClass().getName(), ctx.channel().id());
            super.close(ctx, promise);
        }
    }
}
