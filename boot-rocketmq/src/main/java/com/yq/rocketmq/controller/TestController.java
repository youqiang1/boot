package com.yq.rocketmq.controller;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.rocketmq.constants.RocketMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> test</p>
 * @author youq  2019/9/5 15:17
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @RequestMapping("/produce")
    public String produce(String msg) throws Exception {
        Message message = new Message(RocketMqConstants.TOPIC_YQ_TEST, RocketMqConstants.TAG_YQ_TEST, msg.getBytes(GlobalConstants.UTF8));
        defaultMQProducer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("传输成功：{}", sendResult);
            }

            @Override
            public void onException(Throwable e) {
                log.error("传输失败：", e);
            }
        });
        return GlobalConstants.SUCCESS;
    }

    @RequestMapping("/tranProduce")
    public String tranProduce(String msg) throws Exception {
        Message message = new Message(RocketMqConstants.TOPIC_YQ_TEST, RocketMqConstants.TAG_YQ_TEST, msg.getBytes(GlobalConstants.UTF8));
        /*
         transaction的流程下，rocketmq会先发送一个consumer不可见的消息，然后在调用
         TransactionListener这个接口中的executeLocalTransaction,中的方法执行事务，然后方法内部需要返回
         一个LocalTransactionState的枚举信息，分别为
            COMMIT_MESSAGE, // 提交
            ROLLBACK_MESSAGE, // 回滚
            UNKNOW, // 未知
          相应的当我们返回的是COMMIT_MESSAGE时，那么producer会把消息提交到mq上，
如果      是ROLLBACK_MESSAGE那么producer就会结束，并且不提交到mq，
         */
        transactionMQProducer.sendMessageInTransaction(message, "arg?");
        return GlobalConstants.SUCCESS;
    }

}
