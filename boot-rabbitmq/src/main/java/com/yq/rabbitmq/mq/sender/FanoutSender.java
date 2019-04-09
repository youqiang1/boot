package com.yq.rabbitmq.mq.sender;

import com.yq.kernel.constants.RabbitmqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <p> fanout send</p>
 * @author youq  2019/4/9 19:20
 */
@Slf4j
@Component
public class FanoutSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String message) {
        log.info("topic send: {}", message);
        amqpTemplate.convertAndSend(RabbitmqConstants.EXCHANGE_FANOUT, RabbitmqConstants.ROUTING_KEY_FANOUT, message);
    }

}
