package com.yq.sms.commons.task;

import com.yq.redisop.service.SendMessageContextRedisService;
import com.yq.sms.commons.model.ChannelModel;
import com.yq.sms.commons.sms.packet.SendMessageContext;
import com.yq.sms.commons.spring.BeanFactoryUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 发送线程</p>
 * @author youq  2019/8/26 17:02
 */
@Slf4j
public class SmsSendWindowsThread {

    private ChannelModel model;

    private boolean stopState = true;

    private SendMessageContextRedisService sendMessageContextRedisService = BeanFactoryUtils.getBean(SendMessageContextRedisService.class);

    public SmsSendWindowsThread(ChannelModel model) {
        this.model = model;
        this.stopState = false;
    }

    public void sendMsg(SendMessageContext context) {

    }

}
