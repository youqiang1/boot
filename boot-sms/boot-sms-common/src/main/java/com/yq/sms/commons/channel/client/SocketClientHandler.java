package com.yq.sms.commons.channel.client;

import com.yq.sms.commons.channel.protocol.IChannel;
import com.yq.sms.commons.constants.CmppCommandConstant;
import com.yq.sms.commons.constants.SmsConstants;
import com.yq.sms.commons.enu.ProtocolTypeEnum;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.sms.ChannelCommon;
import com.yq.sms.commons.sms.cmpp20.Cmpp20HeadMessage;
import com.yq.sms.commons.sms.packet.HeadBodyBaseMessage;
import com.yq.sms.commons.util.MessageIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <p> 客户端处理器</p>
 * @author youq  2019/8/23 16:00
 */
@Slf4j
public class SocketClientHandler implements IoHandler {

    private String channelId;

    public SocketClientHandler(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("Client端连接被创建 channelId【{}】", channelId);
        SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
        cfg.setReceiveBufferSize(2 * 1024 * 1024);
        cfg.setReadBufferSize(2 * 1024 * 1024);
        cfg.setKeepAlive(true);
        cfg.setSoLinger(0);
        cfg.setTcpNoDelay(true);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("Client端连接被打开 channelId【{}】", channelId);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("Client端收到服务器端断开连接:channelId【{}】", channelId);
        ChannelCommon.getInstance().channelPoolMap.get(channelId).removeIoSession(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("client session idle，status【{}】", status);
        if (IdleStatus.BOTH_IDLE == status) {
            ChannelModel model = ChannelCommon.getInstance().channelModelMap.get(channelId);
            Object heartbeatNotResp = session.getAttribute(SmsConstants.HEARTBEAT_NOT_RESP);
            if (Objects.nonNull(heartbeatNotResp)) {
                int heartbeatNotRespCount = Integer.parseInt(heartbeatNotResp.toString());
                if (heartbeatNotRespCount < 3) {
                    heartbeatNotRespCount += 1;
                    session.setAttribute(SmsConstants.HEARTBEAT_NOT_RESP, heartbeatNotRespCount);
                } else {
                    log.info("通道【{}】心跳监测三次未响应，关闭连接", model.getName());
                    session.close(false);
                    return;
                }
            }
            if (ProtocolTypeEnum.CMPP20.equals(model.getProtocol())) {
                Cmpp20HeadMessage headMessage = new Cmpp20HeadMessage();
                headMessage.setCommandId(CmppCommandConstant.CMPP_ACTIVE_TEST);
                headMessage.setSequenceId(MessageIdUtil.getInstance().getId());
                headMessage.setTotalLength(12);
                headMessage.objectToByte();
                log.info("通道空闲，心跳监测连接：{}", headMessage.toString());
                session.write(headMessage.bitContent);
            }
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("Exception is happened! channelId【{}】", channelId, cause);
        ChannelCommon.getInstance().channelPoolMap.get(channelId).removeIoSession(session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        try {
            ArrayList<HeadBodyBaseMessage> list = (ArrayList<HeadBodyBaseMessage>) message;
            IChannel iChannel = ChannelCommon.getInstance().channelImplMap.get(channelId);
            iChannel.receive(list, session);
        } catch (Exception e) {
            log.error("message receive exception: ", e);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    }

}
