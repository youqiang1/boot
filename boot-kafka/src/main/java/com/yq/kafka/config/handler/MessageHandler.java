package com.yq.kafka.config.handler;

import com.yq.kafka.proto.user.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p> 消息处理</p>
 * @author youq  2019/4/10 10:35
 */
@Slf4j
@Component
public class MessageHandler {

    /**
     * <p> UserMessage消息处理</p>
     * @param userMessage 消息
     * @author youq  2019/4/10 10:52
     */
    public void process(UserMessage.user userMessage) {
        log.info("singleThread method: {}", userMessage.getMethod());
        log.info("singleThread username: 【{}】", userMessage.getSend().getUsername());
    }

}
