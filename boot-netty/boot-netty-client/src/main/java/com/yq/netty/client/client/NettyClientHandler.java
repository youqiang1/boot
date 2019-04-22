package com.yq.netty.client.client;

import com.yq.netty.commons.constants.NettyConstants;
import com.yq.netty.commons.util.ObjectCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 * @author youq  2019/4/19 19:35
 */
public class NettyClientHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 基于定长的方式解决粘包/拆包问题
        pipeline.addLast(new LengthFieldPrepender(NettyConstants.LENGTH_FIELD_LENGTH));
        pipeline.addLast(
                new LengthFieldBasedFrameDecoder(1024 * 1024, 0,
                        NettyConstants.LENGTH_FIELD_LENGTH, 0, 2)
        );
        // 序列化方式 可使用 MsgPack 或 Protobuf 进行序列化扩展 具体可参考study-netty项目下的相关使用例子
        pipeline.addLast(new ObjectCodec());
        // 心跳机制
        pipeline.addLast(
                new IdleStateHandler(NettyConstants.CLIENT_READER_IDLE_TIME, NettyConstants.CLIENT_WRITER_IDLE_TIME,
                        NettyConstants.CLIENT_ALL_IDLE_TIME, TimeUnit.SECONDS)
        );
        pipeline.addLast(new NettyClient.NettyClientHandlerAdapter());
    }
    
}
