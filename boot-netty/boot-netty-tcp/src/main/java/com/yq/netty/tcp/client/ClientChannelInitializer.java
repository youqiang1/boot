package com.yq.netty.tcp.client;

import com.yq.netty.commons.constants.NettyConstants;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p> 通道初始化，主要用于设置各种Handler</p>
 * @author youq  2019/4/22 14:48
 */
@Component
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ClientChannelHandler clientChannelHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //IdleStateHandler心跳机制,如果超时触发Handler中userEventTrigger()方法
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(NettyConstants.CLIENT_READER_IDLE_TIME,
                        NettyConstants.CLIENT_WRITER_IDLE_TIME,
                        NettyConstants.CLIENT_ALL_IDLE_TIME, TimeUnit.SECONDS));
        //字符串编解码器
        pipeline.addLast(new StringDecoder(), new StringEncoder());
        //自定义Handler
        pipeline.addLast("clientChannelHandler", clientChannelHandler);
    }

}
