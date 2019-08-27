package com.yq.sms.commons.sms;

import com.yq.sms.commons.channel.client.SocketClientHandler;
import com.yq.sms.commons.channel.pool.SocketConnectionPool;
import com.yq.sms.commons.channel.protocol.CMPP20ChannelImpl;
import com.yq.sms.commons.channel.protocol.IChannel;
import com.yq.sms.commons.channel.server.SocketServerHandler;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.task.ChannelTaskInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.service.IoHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 公共类</p>
 * @author youq  2019/8/23 15:28
 */
@Slf4j
public class ChannelCommon {

    public final String STOPPED = "STOPPED";// 停止状态

    public final String RUNNING = "RUNNING";// 运行状态

    public final String EXCEPTION = "EXCEPTION";// 异常状态

    // 内部类构造单例，出于线程安全设计
    private static class SingletonHolder {
        final static ChannelCommon instance = new ChannelCommon();
    }

    public static ChannelCommon getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 通道id-通道实体
     */
    public Map<String, ChannelModel> channelModelMap = new ConcurrentHashMap<>();

    /**
     * 通道id-连接池
     */
    public Map<String, SocketConnectionPool> channelPoolMap = new ConcurrentHashMap<>();

    /**
     * 通道id-通道实现
     */
    public Map<String, IChannel> channelImplMap = new ConcurrentHashMap<>();

    /**
     * 通道id-通道任务列表
     */
    public Map<String, List<ChannelTaskInterface>> channelTaskMap = new ConcurrentHashMap<>();

    public IoHandler getClientIoHandler(ChannelModel channelModel) {
        return new SocketClientHandler(channelModel.getId());
    }

    public IoHandler getServerIoHandler(ChannelModel channelModel) {
        return new SocketServerHandler(channelModel.getId());
    }

    public IChannel getChannel(ChannelModel model) {
        IChannel iChannel = channelImplMap.get(model.getId());
        if (iChannel == null) {
            switch (model.getProtocol()) {
                case CMPP20:
                    iChannel = new CMPP20ChannelImpl();
                    break;
                default:
                    log.info("无法识别的协议类型");
                    break;
            }
        }
        return iChannel;
    }

}
