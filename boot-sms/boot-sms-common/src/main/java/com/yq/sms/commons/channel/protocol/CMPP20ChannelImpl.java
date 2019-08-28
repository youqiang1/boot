package com.yq.sms.commons.channel.protocol;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.yq.redisop.model.SendMessageContextModel;
import com.yq.redisop.service.SendMessageContextRedisService;
import com.yq.sms.commons.channel.pool.SocketConnectionPool;
import com.yq.sms.commons.constants.CmppCommandConstant;
import com.yq.sms.commons.constants.SmsConstants;
import com.yq.sms.commons.enu.ChannelStateEnum;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.model.SendMsgRequest;
import com.yq.sms.commons.service.MoReportService;
import com.yq.sms.commons.service.ResponseService;
import com.yq.sms.commons.sms.ChannelCommon;
import com.yq.sms.commons.sms.cmpp20.*;
import com.yq.sms.commons.sms.packet.HeadBodyBaseMessage;
import com.yq.sms.commons.sms.packet.SendMessageContext;
import com.yq.sms.commons.spring.BeanFactoryUtils;
import com.yq.sms.commons.task.ChannelTaskInterface;
import com.yq.sms.commons.task.SocketSessionCheckThread;
import com.yq.sms.commons.util.DataTypeConvert;
import com.yq.sms.commons.util.MessageIdUtil;
import com.yq.sms.commons.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p> cmpp20协议通道实现</p>
 * @author youq  2019/8/23 17:50
 */
@Slf4j
public class CMPP20ChannelImpl implements IChannel {

    private String channelId;

    private SocketConnectionPool pool;

    private volatile ChannelStateEnum state = ChannelStateEnum.STOP;

    private MoReportService moReportService;

    private ResponseService responseService;

    private ChannelModel channelModel;

    private SendMessageContextRedisService sendMessageContextRedisService = BeanFactoryUtils.getBean(SendMessageContextRedisService.class);

    @Override
    public void init(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public ChannelStateEnum start(ChannelModel model) {
        try {
            this.channelModel = model;
            //创建连接池
            pool = SocketConnectionPool.getConnectionPool(channelId);
            //通道连接检查和维护线程
            SocketSessionCheckThread checkThread = new SocketSessionCheckThread(pool, model);
            checkThread.start();
            Thread checkStartThread = new Thread(checkThread);
            checkStartThread.start();
            ChannelCommon.getInstance().channelTaskMap.get(channelId).add(checkThread);
        } catch (Exception e) {
            log.error("通道启动失败：", e);
        }
        return ChannelStateEnum.STOP;
    }

    @Override
    public void loginBySession(IoSession ioSession, ChannelModel model) {
        try {
            Cmpp20Connect cmpp20Connect = new Cmpp20Connect();
            cmpp20Connect.setSourceAddr(model.getLoginName());
            cmpp20Connect.setPassword(model.getLoginPwd());
            // 版本号
            char[] version = StringUtil.def(model.getVersion(), "20").toCharArray();
            cmpp20Connect.setVersion(((byte) Integer.parseInt(version[0] + "")) << 4 | (byte) Integer.parseInt(version[1] + ""));
            cmpp20Connect.objectToByte();

            Cmpp20HeadMessage headMessage = new Cmpp20HeadMessage();
            headMessage.setCommandId(CmppCommandConstant.CMPP_CONNECT);
            headMessage.setTotalLength(cmpp20Connect.bitContent.length + 12);
            headMessage.setSequenceId(MessageIdUtil.getInstance().getId());
            headMessage.objectToByte();

            byte[] sendByte = DataTypeConvert.GetSendContent(headMessage, cmpp20Connect);
            log.info("【CMPP20_CONNECT】-head【{}】  body【{}】", headMessage.toString(), cmpp20Connect.toString());
            ioSession.write(sendByte);
            while (!ioSession.containsAttribute(SmsConstants.CONNECT_RESP) && ioSession.isConnected()) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ignored) {}
                if (ioSession.containsAttribute(SmsConstants.SESSION_CLOSE)) {
                    break;
                }
            }
            if (!ioSession.containsAttribute(SmsConstants.SESSION_CLOSE)) {
                Cmpp20ConnectResp connectResp = (Cmpp20ConnectResp) ioSession.getAttribute(SmsConstants.CONNECT_RESP);
                if (connectResp == null) {
                    log.info("cmpp2.0协议网关，获取网络状态为空，请检查网关协议是否正确。");
                    return;
                }
                log.info("[cmpp20]协议登录返回头信息，状态报告：{}", connectResp.getStatus());
                if (0 == connectResp.getStatus()) {
                    ioSession.setAttribute(SmsConstants.LOGIN_STATUE, true);
                }
            }
        } catch (Exception e) {
            log.error("cmpp20 connect exception:", e);
        }
    }

    @Override
    public ChannelStateEnum stop() {
        // 销毁任务进程
        List<ChannelTaskInterface> tasks = ChannelCommon.getInstance().channelTaskMap.get(channelId);
        for (ChannelTaskInterface task : tasks) {
            if (task instanceof SocketSessionCheckThread) {
                SocketSessionCheckThread thread = (SocketSessionCheckThread) task;
                thread.close();
            }
            int count = 0;
            while (task.state()) {
                count++;
                task.stop();
                if (count > 10) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }
        //清除任务对象和线程池对象
        ChannelCommon.getInstance().channelTaskMap.remove(channelId);
        ChannelCommon.getInstance().channelPoolMap.remove(channelId);
        if (pool != null) {
            pool = null;
        }
        return state;
    }

    @Override
    public void logoutBySession(IoSession ioSession) {
        Cmpp20HeadMessage headMessage = new Cmpp20HeadMessage();
        headMessage.setCommandId(CmppCommandConstant.CMPP_TERMINATE);
        headMessage.setSequenceId(MessageIdUtil.getInstance().getId());
        headMessage.setTotalLength(12);
        headMessage.objectToByte();
        log.info("断开连接：{}", headMessage.toString());
        ioSession.write(headMessage.bitContent);
    }

    @Override
    public void receive(ArrayList<HeadBodyBaseMessage> messages, IoSession ioSession) {
        ArrayList<SendMessageContext> replyContexts = Lists.newArrayList();
        for (HeadBodyBaseMessage message : messages) {
            byte[] replyBytes = messageProcess(message, ioSession);
            if (replyBytes != null) {
                SendMessageContext context = new SendMessageContext();
                context.setSendContent(replyBytes);
                replyContexts.add(context);
            }
        }
        if (replyContexts.size() > 0) {
            ioSession.write(replyContexts);
        }
    }

    private byte[] messageProcess(HeadBodyBaseMessage message, IoSession ioSession) {
        byte[] replyBytes = null;
        //解析消息头
        Cmpp20HeadMessage headMessage = new Cmpp20HeadMessage();
        headMessage.byteToObject(message.getHead());
        //根据消息类型处理消息体
        switch (headMessage.getCommandId()) {
            case CmppCommandConstant.CMPP_CONNECT_RESP:
                Cmpp20ConnectResp connectResp = new Cmpp20ConnectResp();
                connectResp.byteToObject(message.getContent());
                log.info("连接回复：{}", connectResp);
                if (connectResp.getStatus() != 0) {
                    this.state = ChannelStateEnum.EXCEPTION;
                }
                ioSession.setAttribute(SmsConstants.CONNECT_RESP, connectResp);
                break;
            case CmppCommandConstant.CMPP_TERMINATE_RESP:
                log.info("断开连接回复：{}", DataTypeConvert.bytesToHexString(message.getContent()));
                ChannelCommon.getInstance().channelPoolMap.get(channelId).removeIoSession(ioSession);
                break;
            case CmppCommandConstant.CMPP_SUBMIT_RESP:
                Cmpp20SubmitResp submitResp = new Cmpp20SubmitResp();
                submitResp.byteToObject(message.getContent());
                submitResp.setHeadMessage(headMessage);
                log.info("提交响应：{}", submitResp.toString());
                responseService = new ResponseService();
                responseService.processResponse(submitResp);
                break;
            case CmppCommandConstant.CMPP_DELIVER:
                Cmpp20Deliver deliver = new Cmpp20Deliver();
                deliver.byteToObject(message.getContent());
                log.info("上行|状态报告：{} - {}", headMessage.toString(), deliver.toString());
                Cmpp20DeliverResp deliverResp = new Cmpp20DeliverResp();
                deliverResp.setMsgId(deliver.getMsgId());
                deliverResp.setResult(0);
                deliverResp.objectToByte();
                headMessage.setCommandId(CmppCommandConstant.CMPP_DELIVER_RESP);
                headMessage.setTotalLength(21);
                headMessage.objectToByte();
                replyBytes = DataTypeConvert.GetSendContent(headMessage, deliverResp);
                moReportService = new MoReportService();
                moReportService.processReport(deliver);
                break;
            case CmppCommandConstant.CMPP_ACTIVE_TEST_RESP:
                Cmpp20ActiveTestResp activeTestResp = new Cmpp20ActiveTestResp();
                activeTestResp.byteToObject(message.getContent());
                log.info("心跳响应：{}", activeTestResp.toString());
                //心跳无响应次数清零
                ioSession.setAttribute(SmsConstants.HEARTBEAT_NOT_RESP, 0);
                break;
            case CmppCommandConstant.CMPP_FWD_RESP:
                Cmpp20FwdResp fwdResp = new Cmpp20FwdResp();
                fwdResp.setHeadMessage(headMessage);
                fwdResp.byteToObject(message.getContent());
                log.info("FWD响应：{}", fwdResp.toString());
                responseService = new ResponseService();
                responseService.processResponse(fwdResp);
                break;
            default:
                log.info("错误的命令类型：{}", headMessage.getCommandId());
                break;
        }
        return replyBytes;
    }

    @Override
    public void sendMessage(SendMsgRequest request, String messageId, LocalDateTime submitTime, boolean isFwd) {
        if (isFwd) {
            log.info("FWD消息，messageId: {}", messageId);
            sendMessageForFwd(request, messageId, submitTime);
            return;
        }
        IoSession ioSession = null;
        try {
            ioSession = ChannelCommon.getInstance().channelPoolMap.get(request.getChannelId()).getIoSession();
            if (ioSession == null) {
                log.info("无法获取到连接信息");
                return;
            }
            //手机号
            String[] mobiles = new String[] { request.getMobile() };
            //通道号
            String channelNumber = channelModel.getChannelNumber() + (StringUtils.isEmpty(request.getExtendedCode()) ? "" : request.getExtendedCode());
            //submit实体
            Cmpp20Submit submit = new Cmpp20Submit();
            submit.setPkTotal(1);
            submit.setPkNumber(1);
            submit.setRegisteredDelivery(1);
            submit.setMsgLevel(0);
            submit.setServiceId(channelModel.getServiceCode());
            submit.setFeeUserType(0);
            submit.setFeeTerminalId("");
            submit.setTpPid(0);
            submit.setTpUdhi(0);
            submit.setMsgFmt(15);
            submit.setMsgSrc(channelModel.getSpId());
            submit.setFeeType("01");
            submit.setFeeCode("0000");
            submit.setValidTime("");
            submit.setSrcId(channelNumber);
            submit.setDestUsrTl(1);
            submit.setDestTerminalIds(mobiles);
            submit.setMsgContent(request.getMsg());
            submit.setReserve("");
            submit.setAtTime("");
            List<byte[]> list = CMPP20LongMessage.longMsgProcess(submit);
            if (CollectionUtils.isEmpty(list)) {
                log.info("短信拼接失败。。。");
                return;
            }
            //消息头
            Cmpp20HeadMessage headMessage = new Cmpp20HeadMessage();
            headMessage.setCommandId(CmppCommandConstant.CMPP_SUBMIT);

            List<SendMessageContext> contexts = Lists.newArrayListWithCapacity(list.size());

            for (int i = 0; i < list.size(); i++) {
                headMessage.setSequenceId(MessageIdUtil.getInstance().getId());
                headMessage.setTotalLength(list.get(i).length + 12);
                headMessage.objectToByte();
                submit.bitContent = list.get(i);
                submit.setPkTotal(list.size());
                submit.setPkNumber(i + 1);
                submit.setMsgContent(submit.getMessageToString()[i]);
                submit.setMsgLength(submit.getMessageToString()[i].length());
                byte[] sendBytes = DataTypeConvert.GetSendContent(headMessage, submit);
                HashMap<String, String> sendPackage = new HashMap<>();
                sendPackage.put("mobile", request.getMobile());
                sendPackage.put("addSerial", request.getExtendedCode());
                sendPackage.put("smsId", messageId);
                sendPackage.put("sendTime", System.currentTimeMillis() + "");
                sendPackage.put("userId", request.getUserId());

                SendMessageContext context = new SendMessageContext();
                context.setSendObject(sendPackage);
                context.setSendKey(headMessage.getSequenceId() + "");
                context.setSendContent(sendBytes);
                context.setSubmitTime(submitTime);
                //写redis
                SendMessageContextModel model = new SendMessageContextModel();
                BeanUtils.copyProperties(context, model);
                model.setBillingCount(list.size());
                sendMessageContextRedisService.save(model);

                contexts.add(context);
            }

            if (contexts.size() > 0) {
                log.info("短信开始提交通道: {}", contexts);
                ioSession.write(contexts);
            }
        } finally {
            if (ioSession != null) {
                ChannelCommon.getInstance().channelPoolMap.get(channelId).putIoSession(ioSession);
            }
        }
    }

    /**
     * <p> FWD发送，能发但是整体逻辑有问题，待处理</p>
     * @author youq  2019/8/28 15:22
     */
    private void sendMessageForFwd(SendMsgRequest request, String messageId, LocalDateTime submitTime) {
        IoSession ioSession = null;
        try {
            ioSession = ChannelCommon.getInstance().channelPoolMap.get(request.getChannelId()).getIoSession();
            if (ioSession == null) {
                log.info("无法获取到连接信息");
                return;
            }
            //通道号
            String channelNumber = channelModel.getChannelNumber() + (StringUtils.isEmpty(request.getExtendedCode()) ? "" : request.getExtendedCode());
            //submit实体
            Cmpp20Fwd cmpp20Fwd = new Cmpp20Fwd();
            cmpp20Fwd.setSourceId("123456");
            cmpp20Fwd.setDestinationId("123455");
            cmpp20Fwd.setNodesCount(1);
            cmpp20Fwd.setMsgFwdType(0);
            cmpp20Fwd.setPkTotal(1);
            cmpp20Fwd.setPkNumber(1);
            cmpp20Fwd.setRegisteredDelivery(1);
            cmpp20Fwd.setMsgLevel(0);
            cmpp20Fwd.setServiceId(channelModel.getServiceCode());
            cmpp20Fwd.setFeeUserType(0);
            cmpp20Fwd.setFeeTerminalId("");
            cmpp20Fwd.setTpPid(0);
            cmpp20Fwd.setTpUdhi(0);
            cmpp20Fwd.setMsgFmt(15);
            cmpp20Fwd.setMsgSrc(channelModel.getSpId());
            cmpp20Fwd.setFeeType("01");
            cmpp20Fwd.setFeeCode("0000");
            cmpp20Fwd.setValidTime("");
            cmpp20Fwd.setSrcId(channelNumber);
            cmpp20Fwd.setDestUsrTl(1);
            cmpp20Fwd.setDestId(request.getMobile());
            cmpp20Fwd.setMsgContent(request.getMsg());
            cmpp20Fwd.setReserve("");
            cmpp20Fwd.setAtTime("");
            List<byte[]> list = CMPP20LongMessage.fwdLongMsgProcess(cmpp20Fwd, messageId);
            if (CollectionUtils.isEmpty(list)) {
                log.info("FWD短信拼接失败。。。");
                return;
            }
            //消息头
            Cmpp20HeadMessage headMessage = new Cmpp20HeadMessage();
            headMessage.setCommandId(CmppCommandConstant.CMPP_FWD);

            List<SendMessageContext> contexts = Lists.newArrayListWithCapacity(list.size());

            for (byte[] aList : list) {
                headMessage.setSequenceId(MessageIdUtil.getInstance().getId());
                headMessage.setTotalLength(aList.length + 12);
                headMessage.objectToByte();
                cmpp20Fwd.bitContent = aList;
                byte[] sendBytes = DataTypeConvert.GetSendContent(headMessage, cmpp20Fwd);
                HashMap<String, String> sendPackage = new HashMap<>();
                sendPackage.put("mobile", request.getMobile());
                sendPackage.put("addSerial", request.getExtendedCode());
                sendPackage.put("smsId", messageId);
                sendPackage.put("sendTime", System.currentTimeMillis() + "");
                sendPackage.put("userId", request.getUserId());

                SendMessageContext context = new SendMessageContext();
                context.setSendObject(sendPackage);
                context.setSendKey(headMessage.getSequenceId() + "");
                context.setSendContent(sendBytes);
                context.setSubmitTime(submitTime);

                contexts.add(context);
            }

            if (contexts.size() > 0) {
                log.info("FWD短信开始提交通道: {}", contexts);
                ioSession.write(contexts);
            }
        } finally {
            if (ioSession != null) {
                ChannelCommon.getInstance().channelPoolMap.get(channelId).putIoSession(ioSession);
            }
        }
    }

    @Override
    public void setState(ChannelStateEnum state) {
        this.state = state;
    }

    @Override
    public ChannelStateEnum getState() {
        return state;
    }

}
