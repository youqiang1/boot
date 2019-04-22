package com.yq.netty.tcp.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p> channel handler</p>
 * @author youq  2019/4/22 13:22
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * <p> 获取客户端的消息，开始处理</p>
     * @param ctx ChannelHandlerContext
     * @param msg 客户端消息内容
     * @author youq  2019/4/22 14:07
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("netty tcp 服务接收消息：{}", msg);
        ctx.channel().writeAndFlush("服务端的消息回复").syncUninterruptibly();
    }

    /**
     * <p> 活跃的，有效的通道，第一次连接成功后被调用的方法</p>
     * @param ctx ChannelHandlerContext
     * @author youq  2019/4/22 14:37
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("客户端【{}】连接成功", getRemoteAddress(ctx));
        NettyTcpServer.map.put(getRemoteAddress(ctx), ctx.channel());
    }

    /**
     * <p> 不活动的通道，连接丢失后执行的方法（client端可据此实现断线重连）</p>
     * @param ctx ChannelHandlerContext
     * @author youq  2019/4/22 14:38
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】断开连接", getRemoteAddress(ctx));
        //删除Channel Map中的失效Client
        NettyTcpServer.map.remove(getRemoteAddress(ctx));
        ctx.close();
    }

    /**
     * <p> 异常处理，关闭连接</p>
     * @param ctx   ChannelHandlerContext
     * @param cause Throwable
     * @author youq  2019/4/22 14:39
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("通道【{}】发生异常，即将断开重连", getRemoteAddress(ctx));
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
            switch (event.state()) {
                //读超时，断开
                case READER_IDLE:
                    log.info("client【{}】 READER_IDLE", socketString);
                    ctx.disconnect();
                    break;
                //写超时，断开
                case WRITER_IDLE:
                    log.info("client【{}】 WRITER_IDLE", socketString);
                    ctx.disconnect();
                    break;
                //超时，断开
                case ALL_IDLE:
                    log.info("client【{}】 ALL_IDLE", socketString);
                    ctx.disconnect();
                    break;
                default:
                    log.info("client【{}】 state:【{}】", socketString, event.state());
                    break;
            }
        }
    }

    /**
     * <p> 获取client对象</p>
     * @param ctx ChannelHandlerContext
     * @return java.lang.String
     * @author youq  2019/4/22 14:10
     */
    private String getRemoteAddress(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }

    /**
     * <p> 获取client的ip</p>
     * @param ctx ChannelHandlerContext
     * @return java.lang.String
     * @author youq  2019/4/22 14:09
     */
    public String getIPString(ChannelHandlerContext ctx) {
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        return socketString.substring(1, colonAt);
    }

}
