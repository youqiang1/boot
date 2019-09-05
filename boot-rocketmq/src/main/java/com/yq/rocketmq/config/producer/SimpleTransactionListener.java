package com.yq.rocketmq.config.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 事物监听</p>
 * @author youq  2019/9/5 16:50
 */
@Slf4j
@Configuration
public class SimpleTransactionListener implements TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("execute msg: {}, arg: {}", new String(msg.getBody()), arg);
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("check msg: {}", new String(msg.getBody()));
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
