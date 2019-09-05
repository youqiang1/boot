package com.yq.rocketmq.config.producer;

import com.yq.rocketmq.config.RocketMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * <p> 生产者配置</p>
 * @author youq  2019/9/5 14:47
 */
@Slf4j
@Configuration
public class ProducerConfig {

    @Autowired
    private RocketMqProperties rocketMqProperties;

    @Autowired
    private SimpleTransactionListener transactionListener;

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "default", havingValue = "true")
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        log.info("start rocketmq producer create");
        DefaultMQProducer producer = new DefaultMQProducer(rocketMqProperties.getGroupName());
        producer.setNamesrvAddr(rocketMqProperties.getNameSrvAddr());
        producer.setVipChannelEnabled(false);
        producer.setRetryTimesWhenSendAsyncFailed(10);
        producer.start();
        log.info("finish rocketmq producer create");
        return producer;
    }

    @Bean
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        log.info("start rocketmq transaction producer create");
        ExecutorService executorService = new ThreadPoolExecutor(
                2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024), r -> {
                    Thread thread = new Thread(r);
                    thread.setName("transactionMQProducerThread");
                    return thread;
                });

        TransactionMQProducer producer = new TransactionMQProducer(rocketMqProperties.getTranGroupName());
        producer.setNamesrvAddr(rocketMqProperties.getNameSrvAddr());
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.setSendMsgTimeout(10 * 1000);
        producer.start();
        log.info("finish rocketmq transaction producer create");
        return producer;
    }

}
