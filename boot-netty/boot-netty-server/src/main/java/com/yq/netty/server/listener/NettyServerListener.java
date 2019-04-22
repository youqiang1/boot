package com.yq.netty.server.listener;

import com.yq.netty.commons.util.ObjectCodec;
import com.yq.netty.server.adapter.ServerChannelHandlerAdapter;
import com.yq.netty.server.config.NettyServerConfig;
import com.yq.netty.commons.constants.NettyConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * <p> netty监听</p>
 * @author youq  2019/4/19 14:43
 */
@Slf4j
@Component
public class NettyServerListener {

    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    private EventLoopGroup boss = new NioEventLoopGroup();

    private EventLoopGroup work = new NioEventLoopGroup();

    @Autowired
    private ServerChannelHandlerAdapter channelHandlerAdapter;

    @Autowired
    private NettyServerConfig nettyServerConfig;

    @PreDestroy
    public void close() {
        log.info("关闭服务器。。。");
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    /**
     * <p> 开启服务线程</p>
     * @author youq  2019/4/19 15:55
     */
    public void start() {
        //服务监听的端口
        int port = nettyServerConfig.getPort();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO));
        try {
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //添加心跳支持
                    pipeline.addLast(
                            new IdleStateHandler(NettyConstants.SERVER_READER_IDLE_TIME,
                                    NettyConstants.SERVER_WRITER_IDLE_TIME,
                                    NettyConstants.SERVER_ALL_IDLE_TIME, TimeUnit.SECONDS)
                    );
                    // 基于定长的方式解决粘包/拆包问题
                    pipeline.addLast(
                            new LengthFieldBasedFrameDecoder(NettyConstants.MAX_FRAME_LENGTH, 0,
                                    NettyConstants.LENGTH_FIELD_LENGTH, 0, 2)
                    );
                    pipeline.addLast(new LengthFieldPrepender(NettyConstants.LENGTH_FIELD_LENGTH));
                    //序列化
                    pipeline.addLast(new ObjectCodec());
                    pipeline.addLast(channelHandlerAdapter);
                }
            });
            log.info("netty服务器启动，监听端口【{}】", port);
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("开启服务线程出现异常，释放资源：", e);
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

}
