package com.yq.rabbitmq.mq.receiver;

import com.yq.kernel.constants.RabbitmqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p> topic receiver</p>
 * @author youq  2019/4/9 19:10
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitmqConstants.QUEUE_TOPICA)
public class TopicReceiverA {

    @RabbitHandler
    public void process(String message) {
        log.info("topic.A receiver: {}", message);
    }

}
