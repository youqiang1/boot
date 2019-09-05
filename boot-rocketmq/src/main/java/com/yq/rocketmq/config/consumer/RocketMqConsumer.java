package com.yq.rocketmq.config.consumer;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.rocketmq.constants.RocketMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p> 消费者</p>
 * @author youq  2019/9/5 15:08
 */
@Slf4j
@Configuration
public class RocketMqConsumer extends AbstractConsumerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    ConsumeConcurrentlyStatus process(List<MessageExt> msgs) {
        log.info("rocketmq consumer process...");
        for (MessageExt messageExt : msgs) {
            try {
                String msg = new String(messageExt.getBody(), GlobalConstants.UTF8);
                log.info("接收到消息：{}", msg);
            } catch (UnsupportedEncodingException e) {
                log.error("消息处理失败：", e);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("start rocket mq consumer..., event：{}", event.toString());
        try {
            super.listener(RocketMqConstants.TOPIC_YQ_TEST, RocketMqConstants.TAG_YQ_TEST);
        } catch (MQClientException e) {
            log.error("start rocket mq consumer fail", e);
        }
    }

}
