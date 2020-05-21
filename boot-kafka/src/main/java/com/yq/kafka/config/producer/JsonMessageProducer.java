package com.yq.kafka.config.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p> json结构生产者</p>
 *
 * @author youq  2020/5/19 下午9:40
 */
@Slf4j
@Component
public class JsonMessageProducer {

    @Value("${kafka.topic.producer}")
    private String topic;

    @Autowired
    private KafkaProducer<Integer, Object> jsonProducer;

    /**
     * <p> 消息发送</p>
     * @param message 发送的对象
     * @author youq  2019/4/10 11:09
     */
    public void send(Object message) {
        log.info("jsonMessage发送：【{}】", message);
        jsonProducer.send(new ProducerRecord<>(topic, message));
    }

}