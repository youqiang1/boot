package com.yq.cloud.stream.kafka.config.producer;

import com.yq.cloud.stream.kafka.config.channel.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

/**
 * <p> 生产者</p>
 * @author youq  2018/12/21 15:54
 */
@Slf4j
@EnableBinding(Source.class)
public class MessageProducer {

    @Autowired
    private Source source;

    /**
     * <p> 消息发送</p>
     * @param message 消息体
     * @author youq  2018/12/21 15:58
     */
    public void userProducer(String message) {
        log.info("发送消息：{}", message);
        try {
            source.smsOutput().send(MessageBuilder.withPayload(message).build());
        } catch (Exception e) {
            log.error("消息发送失败", e);
        }
    }

}
