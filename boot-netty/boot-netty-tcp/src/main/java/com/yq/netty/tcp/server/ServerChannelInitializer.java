package com.yq.netty.tcp.server;

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
 * @author youq  2019/4/22 13:20
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServerChannelHandler serverChannelHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //IdleStateHandler心跳机制,如果超时触发Handle中userEventTrigger()方法
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(NettyConstants.SERVER_READER_IDLE_TIME,
                        NettyConstants.SERVER_WRITER_IDLE_TIME,
                        NettyConstants.SERVER_ALL_IDLE_TIME, TimeUnit.SECONDS));
        //字符串编解码器，可自定义实体及其编解码器，例：MessageToByteEncoder ByteToMessageDecoder的实现类
        pipeline.addLast(new StringDecoder(), new StringEncoder());
        //自定义handler
        pipeline.addLast("serverChannelHandler", serverChannelHandler);
    }

}
