package com.yq.kafka.config.consumer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yq.kafka.config.handler.UserMessageHandler;
import com.yq.kafka.config.serializer.UserMessageDeserializer;
import com.yq.kafka.proto.user.UserMessage;
import kafka.utils.ShutdownableThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * <p> 消费者（多线程）</p>
 * @author youq  2019/4/10 10:14
 */
@Slf4j
public class UserMessageMultiConsumer extends ShutdownableThread {

    private final KafkaConsumer<Integer, UserMessage.user> consumer;

    private final String topic;

    private ExecutorService pool;

    public UserMessageMultiConsumer(String brokers, String topic, String consumerGroup) {
        super("KafkaUserConsumerMulti", false);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        this.consumer = new KafkaConsumer<>(props, new IntegerDeserializer(), new UserMessageDeserializer());
        this.topic = topic;

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("user-protoMessage-handle-%d")
                .build();
        /*
        AbortPolicy：直接抛出异常。
        CallerRunsPolicy：只用调用者所在线程来运行任务。
        DiscardOldestPolicy：丢弃队列里最近的一个任务，并执行当前任务。
        DiscardPolicy：不处理，丢弃掉。
        当然也可以根据应用场景需要来实现RejectedExecutionHandler接口自定义策略。如记录日志或持久化不能处理的任务。
         */
        pool = new ThreadPoolExecutor(10, 200, 0L,
                TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(1024),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void doWork() {
        try {
            consumer.subscribe(Collections.singletonList(this.topic));
            ConsumerRecords<Integer, UserMessage.user> records = consumer.poll(100);
            for (ConsumerRecord<Integer, UserMessage.user> record : records) {
                if (record != null && record.value() != null) {
                    log.info("multi receive user message:【{}】  -- offset: 【{}】", record.value(), record.offset());
                    try {
                        //多线程不保证消息顺序, corePoolSize可调整
                        pool.execute(new UserMessageHandler(record.value()));
                    } catch (Exception e) {
                        log.error("user message multi handle fail:", e);
                    }
                }
                //处理完成，手动更新offset
                consumer.commitSync();
            }
        } catch (Exception e) {
            log.error("user message multi consumer exception:", e);
        }
    }

}
