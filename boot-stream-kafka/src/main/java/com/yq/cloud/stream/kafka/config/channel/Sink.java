package com.yq.cloud.stream.kafka.config.channel;

import com.yq.kernel.constants.KafkaConstants;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * <p> 输入通道</p>
 * @author youq  2018/12/21 15:43
 */
public interface Sink {

    String USER_INPUT = KafkaConstants.CONSUMER_USER;

    /**
     * <p> 接收队列</p>
     * @return org.springframework.messaging.SubscribableChannel
     * @author youq  2018/12/21 16:12
     */
    @Input(Sink.USER_INPUT)
    SubscribableChannel userInput();

}
