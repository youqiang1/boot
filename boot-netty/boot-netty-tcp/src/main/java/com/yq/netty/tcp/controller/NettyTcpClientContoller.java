package com.yq.netty.tcp.controller;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.netty.tcp.client.NettyTcpClient;
import com.yq.netty.tcp.client.ServerChannelManager;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试</p>
 * @author youq  2019/4/22 15:09
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class NettyTcpClientContoller {

    @Autowired
    private NettyTcpClient nettyTcpClient;

    @GetMapping("/send")
    public String send(String server, String msg) {
        try {
            Channel channel = ServerChannelManager.get(server);
            channel.writeAndFlush(msg);
            return GlobalConstants.SUCCESS;
        } catch (Exception e) {
            return GlobalConstants.FAIL;
        }
    }

    @GetMapping("/connect")
    public String connect() {
        nettyTcpClient.connect();
        return GlobalConstants.SUCCESS;
    }

}
