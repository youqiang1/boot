package com.yq.kafka.config.handler;

import com.yq.kafka.proto.user.UserMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> UserMessage 处理</p>
 * @author youq  2019/4/10 10:48
 */
@Slf4j
public class UserMessageHandler implements Runnable {

    private UserMessage.user userMessage;

    public UserMessageHandler(UserMessage.user userMessage) {
        this.userMessage = userMessage;
    }

    @Override
    public void run() {
        log.info("multithreading username: 【{}】", userMessage.getSend().getUsername());
    }

}
