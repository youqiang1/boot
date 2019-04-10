package com.yq.cloud.stream.kafka.config.consumer;

import com.yq.cloud.stream.kafka.config.channel.Sink;
import com.yq.cloud.stream.kafka.config.handler.MessageHandler;
import com.yq.cloud.stream.kafka.model.UserModel;
import com.yq.kernel.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * <p> 消费者</p>
 * @author youq  2018/12/21 15:59
 */
@Slf4j
@EnableBinding(Sink.class)
public class MessageConsumer {

    @Autowired
    private MessageHandler messageHandler;

    /**
     * <p> 短信类消息消费</p>
     * @param message 消息体
     * @author youq  2018/12/21 16:01
     */
    @StreamListener(Sink.USER_INPUT)
    private void smsConsumer(String message) {
        log.info("【MessageConsumer】接收到消息：{}", message);
        try {
            UserModel user = ObjectUtils.fromJson(message, UserModel.class);
            if (user == null) {
                log.error("【MessageConsumer】错误的消息【{}】，无法识别", message);
                return;
            }
            messageHandler.process(user);
        } catch (Exception e) {
            log.error("【MessageConsumer】消息【{}】反序列化异常：", message, e);
        }
    }

}
