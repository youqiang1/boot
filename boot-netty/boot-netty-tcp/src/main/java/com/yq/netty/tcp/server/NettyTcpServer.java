package com.yq.netty.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 服务端</p>
 * @author youq  2019/4/22 13:16
 */
@Slf4j
@Component
public class NettyTcpServer {

    /**
     * boss事件轮询线程组
     * 处理Accept连接事件的线程，这里线程数设置为1即可，netty处理链接事件默认为单线程，过度设置反而浪费cpu资源
     */
    private EventLoopGroup boss = new NioEventLoopGroup(1);

    /**
     * worker事件轮询线程组
     * 处理handler的工作线程，其实也就是处理IO读写 。线程数据默认为 CPU 核心数乘以2
     */
    private EventLoopGroup worker = new NioEventLoopGroup();

    @Autowired
    private ServerChannelInitializer serverChannelInitializer;

    @Value("${netty.tcp.port}")
    private Integer port;

    /**
     * 与客户端建立连接后得到的通道对象
     */
    private Channel channel;

    /**
     * 存储client的channel的map集合
     */
    public static Map<String, Channel> map = new ConcurrentHashMap<>();

    /**
     * <p> 开启netty tcp server服务</p>
     * @return io.netty.channel.ChannelFuture
     * @author youq  2019/4/22 13:33
     */
    public ChannelFuture start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        //组配置，初始化ServerBootstrap的线程组
        bootstrap.group(boss, worker)
                //构造channel通道工厂，bossGroup的通道，只是负责连接
                .channel(NioServerSocketChannel.class)
                //设置通道处理者ChannelHandler，workerGroup的处理器
                .childHandler(serverChannelInitializer)
                //socket参数，当服务器请求处理程全满时，用于临时存放已完成三次握手请求的队列的最大长度。
                //如果未设置或所设置的值小于1，Java将使用默认值50。
                .option(ChannelOption.SO_BACKLOG, 1024)
                //启用心跳保活机制，tcp，默认2小时发一次心跳
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //Future：异步任务的生命周期，可用来获取任务结果
        //绑定端口，开启监听，同步等待
        ChannelFuture future = bootstrap.bind(port).syncUninterruptibly();
        if (future != null && future.isSuccess()) {
            channel = future.channel();
            log.info("netty tcp 服务开启成功，端口：【{}】", port);
        } else {
            log.error("netty tcp 服务开启失败");
        }
        return future;
    }

    /**
     * <p> 销毁netty tcp服务</p>
     * @author youq  2019/4/22 13:38
     */
    @PreDestroy
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        try {
            Future<?> future = worker.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("netty tcp workerGroup shutdown fail, {}", future.cause());
            }
            Future<?> future1 = boss.shutdownGracefully().await();
            if (!future1.isSuccess()) {
                log.error("netty tcp bossGroup shutdown fail, {}", future1.cause());
            }
        } catch (InterruptedException e) {
            log.error("netty tcp服务销毁出现异常：", e);
        }
        log.info("netty tcp 服务已停止");
    }

}
