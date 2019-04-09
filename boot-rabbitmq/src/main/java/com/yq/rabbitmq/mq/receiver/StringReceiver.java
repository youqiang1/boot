package com.yq.rabbitmq.mq.receiver;

import com.yq.kernel.constants.RabbitmqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p> 接收数据</p>
 * @author youq  2019/4/9 17:26
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitmqConstants.QUEUE_STRING)
public class StringReceiver {

    @RabbitHandler
    public void process(String hello) {
        log.info("receiver: {}", hello);
    }

}
