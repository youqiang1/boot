package com.yq.kafka.config.consumer;

import com.yq.kafka.config.handler.MessageHandler;
import com.yq.kafka.config.serializer.UserMessageDeserializer;
import com.yq.kafka.proto.user.UserMessage;
import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * <p> 消费者</p>
 * @author youq  2019/4/10 10:14
 */
@Slf4j
public class UserMessageSingleConsumer extends ShutdownableThread {

    private final KafkaConsumer<Integer, UserMessage.user> consumer;

    private final String topic;

    private MessageHandler messageHandler;

    public UserMessageSingleConsumer(String brokers, String topic, String consumerGroup, MessageHandler messageHandler) {
        super("KafkaUserConsumerSingle", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        this.consumer = new KafkaConsumer<>(props, new IntegerDeserializer(), new UserMessageDeserializer());
        this.topic = topic;
        this.messageHandler = messageHandler;
    }

    @Override
    public void doWork() {
        try {
            consumer.subscribe(Collections.singletonList(this.topic));
            ConsumerRecords<Integer, UserMessage.user> records = consumer.poll(100);
            for (ConsumerRecord<Integer, UserMessage.user> record : records) {
                if (record != null && record.value() != null) {
                    log.info("single receive user message:【{}】  -- offset: 【{}】", record.value(), record.offset());
                    try {
                        //单线程，保证消息顺序，处理速度需要编码处理
                        messageHandler.process(record.value());
                    } catch (Exception e) {
                        log.error("user message single handle fail:", e);
                    }
                }
                //处理完成，手动更新offset
                consumer.commitSync();
            }
        } catch (Exception e) {
            log.error("user message single consumer exception:", e);
        }
    }

}
