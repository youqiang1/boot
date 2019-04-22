package com.yq.netty.client.listener;

import com.yq.netty.client.client.NettyClient;
import com.yq.netty.client.config.NettyConfig;
import com.yq.netty.client.controller.DemoController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p> netty客户端监听器</p>
 * @author youq  2019/4/19 19:42
 */
@Slf4j
@Order(0)
@Component
public class NettyClientListener implements CommandLineRunner {

    /**
     * netty客户端配置
     */
    @Resource
    private NettyConfig nettyConfig;
    /**
     * 主要用于测试RPC场景的类。集成到自己的业务中就不需要此依赖
     */
    @Resource
    private DemoController demoController;

    @Override
    public void run(String... args) throws Exception {
        log.info("{} -> [准备进行与服务端通信]", this.getClass().getName());
        // region 模拟RPC场景
        Thread t1 = new Thread(() -> {
            try {
                demoController.call();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 使用一个线程模拟Client启动完毕后RPC的场景
        t1.start();
        // endregion
        // 获取服务器监听的端口
        int port = nettyConfig.getPort();
        // 获取服务器IP地址
        String url = nettyConfig.getUrl();
        // 启动NettyClient
        new NettyClient(port, url).start();
    }

}
