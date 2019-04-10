package com.yq.cloud.stream.kafka.config.channel;

import com.yq.kernel.constants.KafkaConstants;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p> 输出通道</p>
 * @author youq  2018/12/21 15:49
 */
public interface Source {

    String USER_OUTPUT = KafkaConstants.PRODUCER_USER;

    /**
     * <p> 发送队列</p>
     * @return org.springframework.messaging.MessageChannel
     * @author youq  2018/12/21 16:11
     */
    @Output(Source.USER_OUTPUT)
    MessageChannel smsOutput();

}
