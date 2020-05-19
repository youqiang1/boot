package com.yq.kafka.config.consumer;

import com.yq.kafka.config.handler.MessageHandler;
import com.yq.kafka.config.serializer.JsonDeserializer;
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
public class JsonConsumer extends ShutdownableThread {

    private final KafkaConsumer<Integer, Object> consumer;

    private final String topic;

    public JsonConsumer(String brokers, String topic, String consumerGroup) {
        super("KafkaJsonConsumer", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        this.consumer = new KafkaConsumer<>(props, new IntegerDeserializer(), new JsonDeserializer());
        this.topic = topic;
    }

    @Override
    public void doWork() {
        try {
            consumer.subscribe(Collections.singletonList(this.topic));
            ConsumerRecords<Integer, Object> records = consumer.poll(100);
            for (ConsumerRecord<Integer, Object> record : records) {
                if (record != null && record.value() != null) {
                    log.info("single receive user message:【{}】  -- offset: 【{}】", record.value(), record.offset());
                    try {
                        log.info("消费到JSON数据：{}", record.value());
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
