package com.yq.kafka.config.producer;

import com.yq.kafka.proto.user.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p> UserMessage producer</p>
 * @author youq  2019/4/10 11:35
 */
@Slf4j
@Component
public class UserMessageProducer {

    @Value("${kafka.topic.producer}")
    private String topic;

    @Autowired
    private KafkaProducer<Integer, UserMessage.user> userProducer;

    /**
     * <p> 消息发送</p>
     * @param userMessage protobuf消息体
     * @author youq  2019/4/10 11:09
     */
    public void send(UserMessage.user userMessage) {
        log.info("userMessage发送：【{}】", userMessage);
        userProducer.send(new ProducerRecord<>(topic, userMessage));
    }

}
