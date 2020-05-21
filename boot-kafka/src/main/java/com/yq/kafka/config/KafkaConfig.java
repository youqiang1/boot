package com.yq.kafka.config;

import com.yq.kafka.config.consumer.JsonConsumer;
import com.yq.kafka.config.consumer.UserMessageMultiConsumer;
import com.yq.kafka.config.consumer.UserMessageSingleConsumer;
import com.yq.kafka.config.handler.MessageHandler;
import com.yq.kafka.config.serializer.JsonSerializer;
import com.yq.kafka.config.serializer.UserMessageSerializer;
import com.yq.kafka.proto.user.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * <p> kafka 配置</p>
 * @author youq  2019/4/10 10:13
 */
@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${kafka.brokers}")
    private String brokers;

    @Value("${kafka.topic.consumer}")
    private String consumerTopic;

    @Value("${kafka.topic.singleConsumerGroupId}")
    private String singleConsumerGroup;

    @Value("${kafka.topic.multiConsumerGroupId}")
    private String multiConsumerGroup;

    @Value("${kafka.enable}")
    private String enable;

    @Autowired
    private MessageHandler messageHandler;
        
    /**
     * <p> consumer对象init 单线程处理</p>
     * @author youq  2019/4/10 10:37
     */
//    @Bean
    public UserMessageSingleConsumer userSingleConsumerStart() {
        UserMessageSingleConsumer messageConsumer =
                new UserMessageSingleConsumer(brokers, consumerTopic, singleConsumerGroup, messageHandler);
        messageConsumer.start();
        return messageConsumer;
    }

    /**
     * <p> consumer对象init 多线程处理</p>
     * @author youq  2019/4/10 10:37
     */
//    @Bean
    public UserMessageMultiConsumer userMultiConsumerStart() {
        UserMessageMultiConsumer messageConsumer =
                new UserMessageMultiConsumer(brokers, consumerTopic, multiConsumerGroup);
        messageConsumer.start();
        return messageConsumer;
    }

    /**
     * <p> producer对象init</p>
     * @author youq  2018/6/5 11:00
     */
    // @Bean
    public KafkaProducer<Integer, UserMessage.user> userProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("client.id", "UserMessageProducer");
        return new KafkaProducer<>(props, new IntegerSerializer(), new UserMessageSerializer());
    }

    /**
     * <p> consumer对象init 多线程处理</p>
     * @author youq  2019/4/10 10:37
     */
    @Bean
    public JsonConsumer jsonConsumerStart() {
        if ("true".equals(enable)) {
            JsonConsumer jsonConsumer = new JsonConsumer(brokers, consumerTopic, singleConsumerGroup);
            jsonConsumer.start();
            return jsonConsumer;
        } else {
            return null;
        }
    }

    /**
     * <p> producer对象init</p>
     * @author youq  2018/6/5 11:00
     */
    @Bean
    // @ConditionalOnProperty(name = "kafka.enable", havingValue = "true", matchIfMissing = true)
    public KafkaProducer<Integer, Object> jsonProducer() {
        if ("true".equals(enable)) {
            Properties props = new Properties();
            props.put("bootstrap.servers", brokers);
            props.put("client.id", "JsonProducerKey");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new KafkaProducer<>(props);
        } else {
            return null;
        }
    }

}
