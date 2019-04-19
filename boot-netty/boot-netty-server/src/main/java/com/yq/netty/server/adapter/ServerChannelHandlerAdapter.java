package com.yq.netty.server.adapter;

import com.yq.netty.commons.entity.MethodInvokeMeta;
import com.yq.netty.server.dispatcher.RequestDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 通道适配器</p>
 * @author youq  2019/4/19 14:46
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerChannelHandlerAdapter extends ChannelHandlerAdapter {

    @Autowired
    private RequestDispatcher dispatcher;

    private int lossConnectCount = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{} -> 连接异常，【{}】通道异常，异常原因：{}",
                this.getClass().getName(), ctx.channel().id(), cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            if ("ping-pong-ping-pong".equals(msg)) {
                log.info("{} -> 心跳监测，【{}】通道活跃", this.getClass().getName(), ctx.channel().id());
                lossConnectCount = 0;
                return;
            }
        }
        MethodInvokeMeta invokeMeta = (MethodInvokeMeta) msg;
        log.info("{} -> 客户短信消息 \n 方法名：{} \n 参数列表： {} \n返回值：{}",
                this.getClass().getName(), invokeMeta.getMethodName(), invokeMeta.getArgs(), invokeMeta.getReturnType());
        this.dispatcher.dispatcher(ctx, invokeMeta);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("{} -> 已经有5秒没有收到客户端消息了", this.getClass().getName());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    log.info("{} -> 释放不活跃的通道：{}", this.getClass().getName(), ctx.channel().id());
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
