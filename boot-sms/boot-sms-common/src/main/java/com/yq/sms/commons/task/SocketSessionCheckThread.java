package com.yq.sms.commons.task;

import com.yq.kernel.enu.OperatorTypeEnum;
import com.yq.sms.commons.channel.pool.SocketConnectionPool;
import com.yq.sms.commons.channel.protocol.IChannel;
import com.yq.sms.commons.constants.ConnectConstants;
import com.yq.sms.commons.enu.ChannelStateEnum;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.sms.ChannelCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;

/**
 * <p> 通道连接检查和维护线程</p>
 * @author youq  2019/8/26 11:42
 */
@Slf4j
public class SocketSessionCheckThread extends ChannelTaskInterface {

    private SocketConnectionPool pool;

    private ChannelModel model;

    public SocketSessionCheckThread(SocketConnectionPool pool, ChannelModel channelModel) {
        this.pool = pool;
        this.model = channelModel;
    }

    @Override
    public void Loop() {
        connectAndLogin(model.getOperatorType());
    }

    private void connectAndLogin(OperatorTypeEnum operator) {
        IChannel iChannel = ChannelCommon.getInstance().channelImplMap.get(model.getId());
        if (iChannel == null) {
            return;
        }
        int socketSize = model.getSocketNumber();
        if (pool.getIoSessionSize() < socketSize) {
            for (int i = 0; i < socketSize - pool.getIoSessionSize(); i++) {
                //连接、登陆
                log.info("开始连接通道，ip: {}, port: {}, operator: {}", model.getHost(), model.getPort(), operator);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(model.getHost(), model.getPort());
                IoSession ioSession = pool.connector.connect(inetSocketAddress).awaitUninterruptibly().getSession();
                if (ioSession == null) {
                    log.info("连接通道失败，ip: {}, port: {}", model.getHost(), model.getPort());
                    return;
                }
                ioSession.getConfig().setBothIdleTime(30);
                log.info("通道连接成功，准备开始登陆");
                //登陆
                iChannel.loginBySession(ioSession, model);
                if (ioSession.containsAttribute(ConnectConstants.LOGIN_STATUE)) {
                    pool.addIoSession(ioSession);
                    iChannel.setState(ChannelStateEnum.RUNNING);
                    log.info("登陆成功，ip: {}, port: {}", model.getHost(), model.getPort());
                } else {
                    pool.removeIoSession(ioSession);
                    log.info("登陆失败，ip: {}, port: {}", model.getHost(), model.getPort());
                }
            }
        }
    }

}
