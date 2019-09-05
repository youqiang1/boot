package com.yq.rocketmq.config.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p> 消费者抽象类</p>
 * @author youq  2019/9/5 15:00
 */
@Slf4j
@Configuration
public abstract class AbstractConsumerConfig {

    @Value("${rocketmq.consumer.groupName}")
    private String consumerGroupName;

    @Value("${rocketmq.consumer.nameSrvAddr}")
    private String nameSrvAddr;

    public void listener(String topic, String tag) throws MQClientException {
        log.info("开启【{}:{}】的消费者", topic, tag);
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.subscribe(topic, tag);
        //监听
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> process(msgs));
        consumer.start();
        log.info("rocketmq服务监听启动成功");
    }

    abstract ConsumeConcurrentlyStatus process(List<MessageExt> msgs);

}
