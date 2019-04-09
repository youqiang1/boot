package com.yq.rabbitmq.mq.sender;

import com.yq.kernel.constants.RabbitmqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> topic send</p>
 * @author youq  2019/4/9 19:05
 */
@Slf4j
@Component
public class TopicSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String message) {
        log.info("topic A send: {}", message);
        amqpTemplate.convertAndSend(RabbitmqConstants.EXCHANGE_TOPIC, RabbitmqConstants.ROUTING_KEY_TOPICA, message);
    }

    public void sendB(String message) {
        log.info("topic B send: {}", message);
        amqpTemplate.convertAndSend(RabbitmqConstants.EXCHANGE_TOPIC, RabbitmqConstants.ROUTING_KEY_TOPICB, message);
    }

}
