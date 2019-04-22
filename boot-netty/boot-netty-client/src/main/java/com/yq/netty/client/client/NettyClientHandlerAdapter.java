package com.yq.netty.client.client;

import com.yq.netty.client.util.ChannelUtil;
import com.yq.netty.commons.constants.NettyConstants;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 自定义的NettyClientHandler</p>
 * @author youq  2019/4/22 10:06
 */
@Slf4j
public class NettyClientHandlerAdapter extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{} -> [通道异常] {}", this.getClass().getName(), ctx.channel().id());
        ChannelUtil.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("{} -> [连接建立成功] {}", this.getClass().getName(), channelHandlerContext.channel().id());
        // 注册通道
        ChannelUtil.registerChannel(channelHandlerContext.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof Exception)) {
            log.info("{} -> [客户端收到的消息] {}", this.getClass().getName(), msg);
        }
        String longText = ctx.channel().id().asLongText();
        String resultKey = ChannelUtil.getResultKey(longText);
        ChannelUtil.calculateResult(resultKey, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("{} -> [客户端消息接收完毕] {}", this.getClass().getName(), ctx.channel().id());
        super.channelReadComplete(ctx);
        boolean active = ctx.channel().isActive();
        log.info("{} -> [此时通道状态] {}", this.getClass().getName(), active);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("{} -> [客户端心跳监测发送] 通道编号：{}", this.getClass().getName(), ctx.channel().id());
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(NettyConstants.HEARTBEAT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        log.info("{} -> [关闭通道] {}", this.getClass().getName(), ctx.channel().id());
        super.close(ctx, promise);
//        ChannelUtil.remove(ctx.channel());
    }
}