package com.yq.rabbitmq.mq.sender;

import com.yq.kernel.constants.RabbitmqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <p> 带回调的 sender</p>
 * @author youq  2019/4/9 19:43
 */
@Slf4j
@Component
public class CallBackSender implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplateNew;

    public void send(String message) {
        rabbitTemplateNew.setConfirmCallback(this);
        log.info("callback sender send: {}", message);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("callback sender UUID: {}", correlationData.getId());
        rabbitTemplateNew.convertAndSend(
                RabbitmqConstants.EXCHANGE_TOPIC, RabbitmqConstants.ROUTING_KEY_TOPICB, message, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("callback confirm: {}, ack: {}, cause: {}", correlationData.getId(), ack, cause);
    }

}
