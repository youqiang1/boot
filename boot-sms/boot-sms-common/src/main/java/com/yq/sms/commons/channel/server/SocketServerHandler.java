package com.yq.sms.commons.channel.server;

import com.yq.sms.commons.sms.ChannelCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * <p> 服务端处理器</p>
 * @author youq  2019/8/23 16:50
 */
@Slf4j
public class SocketServerHandler implements IoHandler {

    private String channelId;

    public SocketServerHandler(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("Server端连接被创建");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("Server端连接被打开");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("Server端连接被关闭");
        ChannelCommon.getInstance().channelPoolMap.get(channelId).removeIoSession(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("Server端连接被空闲");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.info("Server端连接异常");
        ChannelCommon.getInstance().channelPoolMap.get(channelId).removeIoSession(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.info("message sent");
    }

}
