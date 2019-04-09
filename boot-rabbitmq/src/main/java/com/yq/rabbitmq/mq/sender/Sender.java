package com.yq.rabbitmq.mq.sender;

import com.yq.kernel.constants.RabbitmqConstants;
import com.yq.rabbitmq.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 消息发送</p>
 * @author youq  2019/4/9 17:27
 */
@Slf4j
@Component
public class Sender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String message) {
        log.info("send message: {}", message);
        amqpTemplate.convertAndSend(RabbitmqConstants.QUEUE_STRING, message);
    }

    public void send(String queue, Object content) {
        log.info("send 【{}】 to 【{}】", content, queue);
        if (content.getClass().equals(String.class)) {
            amqpTemplate.convertAndSend(queue, content);
        } else {
            amqpTemplate.convertAndSend(queue, content.toString());
        }
    }

    public void sendUser(User user) {
        log.info("send user : {}", user);
        amqpTemplate.convertAndSend(RabbitmqConstants.QUEUE_USER, user);
    }

}
