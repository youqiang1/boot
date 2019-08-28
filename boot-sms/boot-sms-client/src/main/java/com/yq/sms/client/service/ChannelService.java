package com.yq.sms.client.service;

import com.alibaba.druid.util.StringUtils;
import com.yq.redisop.model.SendMessageModel;
import com.yq.redisop.service.SendMessageContextRedisService;
import com.yq.redisop.service.SendMessageRedisService;
import com.yq.sms.client.controller.request.ChannelEditRequest;
import com.yq.sms.client.entity.ChannelInfo;
import com.yq.sms.client.repository.ChannelRepository;
import com.yq.sms.commons.channel.protocol.IChannel;
import com.yq.sms.commons.enu.ChannelStateEnum;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.model.SendMsgRequest;
import com.yq.sms.commons.sms.ChannelCommon;
import com.yq.sms.commons.util.MessageIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p> 通道信息service</p>
 * @author youq  2019/8/23 17:27
 */
@Slf4j
@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private SendMessageRedisService sendMessageRedisService;

    @Autowired
    private SendMessageContextRedisService sendMessageContextRedisService;

    /**
     * <p> 根据通道id启动通道</p>
     * @param channelId 通道id
     * @return boolean
     * @author youq  2019/8/26 10:08
     */
    public boolean start(String channelId) {
        try {
            ChannelInfo channelInfo = channelRepository.findOne(Integer.parseInt(channelId));
            if (channelInfo == null) {
                return false;
            }
            ChannelModel model = new ChannelModel();
            BeanUtils.copyProperties(channelInfo, model);
            model.setId(channelInfo.getId().toString());
            //通道配置信息写入ChannelCommon
            ChannelCommon.getInstance().channelModelMap.put(channelId, model);
            ChannelCommon.getInstance().channelTaskMap.put(channelId, new ArrayList<>());
            IChannel iChannel = ChannelCommon.getInstance().getChannel(model);
            if (iChannel == null) {
                log.info("获取通道实现对象失败");
                return false;
            }
            //通道实现类信息写入ChannelCommon
            ChannelCommon.getInstance().channelImplMap.put(channelId, iChannel);
            iChannel.init(channelId);
            iChannel.start(model);
            //启动等待
            int count = 1;
            while (!ChannelStateEnum.RUNNING.equals(iChannel.getState()) && count <= 10) {
                log.info("通道启动等待...{}", count);
                Thread.sleep(1000);
                count += 1;
            }
            ChannelStateEnum state = iChannel.getState();
            if (ChannelStateEnum.RUNNING.equals(state)) {
                log.info("通道【{}】启动成功", model.getName());
                return true;
            }
            log.info("通道【{}】启动失败", model.getName());
        } catch (Exception e) {
            log.error("channel start exception: ", e);
        }
        return false;
    }

    /**
     * <p> 根据通道id停止通道</p>
     * @param channelId 通道id
     * @return boolean
     * @author youq  2019/8/26 10:08
     */
    public boolean stop(String channelId) {
        try {
            ChannelInfo channelInfo = channelRepository.findOne(Integer.parseInt(channelId));
            if (channelInfo == null) {
                return false;
            }
            ChannelModel model = new ChannelModel();
            BeanUtils.copyProperties(channelInfo, model);
            model.setId(channelInfo.getId().toString());
            synchronized (model.getChannelNumber()) {
                IChannel iChannel = ChannelCommon.getInstance().channelImplMap.get(channelId);
                if (iChannel == null) {
                    return true;
                }
                iChannel.setState(ChannelStateEnum.STOP);
                iChannel.stop();
                ChannelCommon.getInstance().channelImplMap.remove(channelId);
                log.info("停止通道【{}】完成", model.getName());
                return true;
            }
        } catch (Exception e) {
            log.error("channel stop exception: ", e);
        }
        return false;
    }

    /**
     * <p> 添加通道信息</p>
     * @param request 请求参数
     * @return boolean
     * @author youq  2019/8/26 10:09
     */
    public boolean add(ChannelEditRequest request) {
        log.info("添加通道信息请求参数：{}", request);
        if (StringUtils.isEmpty(request.getName())) {
            log.info("请求参数异常");
            return false;
        }
        ChannelInfo channelInfo = new ChannelInfo();
        BeanUtils.copyProperties(request, channelInfo);
        channelInfo.setState(ChannelStateEnum.STOP);
        channelRepository.save(channelInfo);
        return true;
    }

    /**
     * <p> 发送短信</p>
     * @param request 请求参数
     * @param isFwd   是否FWD消息
     * @return boolean
     * @author youq  2019/8/26 14:41
     */
    public boolean sendMsg(SendMsgRequest request, boolean isFwd) {
        boolean flag = verify(request);
        if (!flag) {
            return false;
        }
        String messageId = MessageIdUtil.getInstance().getMessageId();
        LocalDateTime submitTime = LocalDateTime.now();
        //已发信息入redis
        if (!isFwd) {
            SendMessageModel model = SendMessageModel.builder()
                    .key(messageId + request.getMobile())
                    .messageId(messageId)
                    .mobile(request.getMobile())
                    .userId(request.getUserId())
                    .msg(request.getMsg())
                    .channelId(request.getChannelId())
                    .extendedCode(request.getExtendedCode())
                    .submitTime(submitTime)
                    .saveFlag(false)
                    .build();
            sendMessageRedisService.save(model);
        }
        //发送
        IChannel iChannel = ChannelCommon.getInstance().channelImplMap.get(request.getChannelId());
        iChannel.sendMessage(request, messageId, submitTime, isFwd);
        return true;
    }

    private boolean verify(SendMsgRequest request) {
        if (request == null || StringUtils.isEmpty(request.getChannelId()) || StringUtils.isEmpty(request.getMsg())) {
            log.info("请求参数异常");
            return false;
        }
        if (StringUtils.isEmpty(request.getMobile()) || request.getMobile().length() != 11) {
            log.info("接收手机号码异常");
            return false;
        }
        IChannel iChannel = ChannelCommon.getInstance().channelImplMap.get(request.getChannelId());
        if (iChannel == null) {
            log.info("通道未启动，无法发送");
            return false;
        }
        int ioSessionSize = ChannelCommon.getInstance().channelPoolMap.get(request.getChannelId()).getIoSessionSize();
        if (ioSessionSize <= 0) {
            log.info("通道暂无连接，无法发送");
            return false;
        }
        return true;
    }

}
