package com.yq.rabbitmq.mq.receiver;

import com.yq.kernel.constants.RabbitmqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p> fanout receiver</p>
 * @author youq  2019/4/9 19:10
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitmqConstants.QUEUE_FANOUTA)
public class FanoutReceiverA {

    @RabbitHandler
    public void process(String message) {
        log.info("fanout.A receiver: {}", message);
    }

}
