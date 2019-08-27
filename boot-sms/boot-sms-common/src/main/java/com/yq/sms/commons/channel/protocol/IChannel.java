package com.yq.sms.commons.channel.protocol;

import com.yq.sms.commons.enu.ChannelStateEnum;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.model.SendMsgRequest;
import com.yq.sms.commons.sms.packet.HeadBodyBaseMessage;
import org.apache.mina.core.session.IoSession;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p> 通道实现接口</p>
 * @author youq  2019/8/23 17:49
 */
public interface IChannel {

    void init(String channelId);

    ChannelStateEnum start(ChannelModel model);

    ChannelStateEnum stop();

    void loginBySession(IoSession ioSession, ChannelModel model);

    void logoutBySession(IoSession ioSession);

    /**
     * <p> 消息处理</p>
     * @param messages  消息
     * @param ioSession IoSession
     * @author youq  2019/8/26 12:25
     */
    void receive(ArrayList<HeadBodyBaseMessage> messages, IoSession ioSession);

    /**
     * <p> 短信发送</p>
     * @param request    SendMsgRequest
     * @param messageId  消息id
     * @param submitTime 提交时间
     * @author youq  2019/8/26 16:18
     */
    void sendMessage(SendMsgRequest request, String messageId, LocalDateTime submitTime);

    void setState(ChannelStateEnum state);

    ChannelStateEnum getState();

}
