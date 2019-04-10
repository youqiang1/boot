package com.yq.cloud.stream.kafka.service;

import com.yq.cloud.stream.kafka.config.producer.MessageProducer;
import com.yq.cloud.stream.kafka.model.UserModel;
import com.yq.kernel.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 用户service</p>
 * @author youq  2019/4/10 15:05
 */
@Service
public class UserService {

    @Autowired
    private MessageProducer messageProducer;

    /**
     * <p> 消息发送</p>
     * @param user 发送的实体
     * @author youq  2019/4/10 15:06
     */
    public void send(UserModel user) {
        messageProducer.userProducer(ObjectUtils.toJson(user));
    }

}
