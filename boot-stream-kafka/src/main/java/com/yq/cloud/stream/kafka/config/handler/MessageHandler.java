package com.yq.cloud.stream.kafka.config.handler;

import com.yq.cloud.stream.kafka.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p> 消息处理</p>
 * @author youq  2018/12/24 10:52
 */
@Slf4j
@Component
public class MessageHandler {

    /**
     * <p> 消息处理</p>
     * @param user UserModel
     * @author youq  2019/4/10 15:02
     */
    public void process(UserModel user) {
        log.info("user: {}", user);
    }

}
