package com.yq.sms.commons.channel.pool;

import com.yq.sms.commons.channel.codec.HeadProcessProtocolCodecFactory;
import com.yq.sms.commons.constants.SmsConstants;
import com.yq.sms.commons.enu.ProtocolTypeEnum;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.sms.ChannelCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p> 连接池</p>
 * @author youq  2019/8/23 15:37
 */
@Slf4j
public class SocketConnectionPool {

    public NioSocketConnector connector;

    public IoAcceptor acceptor;

    public List<IoSession> ioSessions = Collections.synchronizedList(new ArrayList<>());

    private LinkedBlockingQueue<IoSession> sessionQueue = new LinkedBlockingQueue<>();

    public SocketConnectionPool(ChannelModel channelModel) {
        try {
            log.info("通道【{}】长连接开始创建，运营商【{}】", channelModel.getName(), channelModel.getOperatorType());
            //客户端创建
            connector = new NioSocketConnector();
            ProtocolCodecFactory _ProtocolCodecFactory = new HeadProcessProtocolCodecFactory(channelModel.getOperatorType());
            // 设置编码过滤器
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(_ProtocolCodecFactory));
            connector.setConnectTimeoutMillis(10000L);
            IoHandler clientHandler = ChannelCommon.getInstance().getClientIoHandler(channelModel);
            // 设置事件处理器
            connector.setHandler(clientHandler);
            //联通需要创建双向通道，此处再创建服务监听
            if (ProtocolTypeEnum.SGIP12.equals(channelModel.getProtocol())
                    || ProtocolTypeEnum.CMPP20FWD.equals(channelModel.getProtocol())) {
                acceptor = new NioSocketAcceptor();
                ProtocolCodecFactory serverProtocolCodecFactory = new HeadProcessProtocolCodecFactory(channelModel.getOperatorType());
                // 设置编码过滤器
                acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(serverProtocolCodecFactory));
                IoHandler serverHandler = ChannelCommon.getInstance().getServerIoHandler(channelModel);
                // 指定业务逻辑处理器
                acceptor.setHandler(serverHandler);
                // 监听
                String ListenIP = channelModel.getListenHost();
                ListenIP = null == ListenIP || "".equals(ListenIP) ? "0.0.0.0" : ListenIP;
                acceptor.setDefaultLocalAddress(new InetSocketAddress(ListenIP, channelModel.getListenPort()));
                acceptor.bind();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (log.isInfoEnabled()) {
                    log.debug("开始关闭系统。。。");
                }
                if (connector != null) {
                    for (IoSession session : connector.getManagedSessions().values()) {
                        try {
                            session.close(true).await();
                        } catch (InterruptedException e) {
                            log.error("session close Exception is happened!", e);
                        }
                    }
                    connector.dispose();
                }

                if (acceptor != null) {
                    for (IoSession session : acceptor.getManagedSessions().values()) {
                        try {
                            session.close(true).await();
                        } catch (InterruptedException e) {
                            log.error("session close Exception happened!", e);
                        }
                    }
                    acceptor.unbind();
                    acceptor.dispose();
                }

            }));
        } catch (IOException e) {
            log.error("socket connection pool create exception: ", e);
        }
    }

    /**
     * <p> 根据通道id获取或者创建一个连接池</p>
     * @param channelId 通道id
     * @return com.yq.sms.commons.channel.pool.SocketConnectionPool
     * @author youq  2019/8/23 15:42
     */
    public static SocketConnectionPool getConnectionPool(String channelId) {
        Map<String, SocketConnectionPool> channelPoolMap = ChannelCommon.getInstance().channelPoolMap;
        if (channelPoolMap != null && channelPoolMap.containsKey(channelId)) {
            return channelPoolMap.get(channelId);
        } else {
            ChannelModel channelModel = ChannelCommon.getInstance().channelModelMap.get(channelId);
            if (channelModel == null) {
                log.info("找不到该通道信息，无法进行通道创建");
                return null;
            }
            SocketConnectionPool pool = new SocketConnectionPool(channelModel);
            ChannelCommon.getInstance().channelPoolMap.put(channelId, pool);
            return pool;
        }
    }

    /**
     * <p> 添加连接</p>
     * @param ioSession IoSession
     * @author youq  2019/8/23 20:38
     */
    public synchronized void addIoSession(IoSession ioSession) {
        ioSessions.add(ioSession);
        sessionQueue.add(ioSession);
    }

    /**
     * <p> 断开&清除连接</p>
     * @param ioSession IoSession
     * @author youq  2019/8/23 20:38
     */
    public synchronized void removeIoSession(IoSession ioSession) {
        ioSession.setAttribute(SmsConstants.SESSION_CLOSE, true);
        ioSessions.remove(ioSession);
        boolean flag = sessionQueue.remove(ioSession);
        if (flag) {
            if (ioSession.containsAttribute(SmsConstants.READ_CONTENT)) {
                try {
                    IoBuffer ioBuffer = (IoBuffer) ioSession.getAttribute(SmsConstants.READ_CONTENT);
                    ioBuffer.clear();
                } catch (Exception e) {
                    log.error("removeIoSession exception: ", e);
                }
            }
            ioSession.close(true);
        }
    }

    /**
     * <p> 获取通道连接数</p>
     * @return int 通道连接数
     * @author youq  2019/8/26 14:48
     */
    public int getIoSessionSize() {
        return ioSessions.size();
    }

    /**
     * <p> 获取IoSession信息</p>
     * @return org.apache.mina.core.session.IoSession
     * @author youq  2019/8/26 17:48
     */
    public IoSession getIoSession() {
        try {
            return sessionQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    /**
     * <p> 添加IoSession到指定通道id的队列</p>
     * @param ioSession IoSession
     * @author youq  2019/8/27 12:30
     */
    public void putIoSession(IoSession ioSession) {
        sessionQueue.add(ioSession);
    }

}
