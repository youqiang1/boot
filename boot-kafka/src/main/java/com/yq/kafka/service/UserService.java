package com.yq.kafka.service;

import com.yq.kafka.config.model.JsonRequest;
import com.yq.kafka.config.producer.JsonMessageProducer;
import com.yq.kafka.config.producer.UserMessageProducer;
import com.yq.kafka.proto.user.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 用户service</p>
 * @author youq  2019/4/10 11:04
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMessageProducer userMessageProducer;

    @Autowired
    private JsonMessageProducer jsonMessageProducer;

    /**
     * <p> 消息发送</p>
     * @param userMessage protobuf消息体
     * @author youq  2019/4/10 11:09
     */
    public void send(UserMessage.user userMessage) {
        userMessageProducer.send(userMessage);
    }

    public void sendForJson(JsonRequest request) {
        jsonMessageProducer.send(request);
    }

}
