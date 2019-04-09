package com.yq.rabbitmq.schedule;

import com.yq.rabbitmq.mq.sender.Sender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p> 测试</p>
 * @author youq  2019/4/9 17:29
 */
@Slf4j
@Component
public class TestSchedule {

    @Autowired
    private Sender sender;

    @Scheduled(fixedDelay = 10 * 1000)
    public void executor() {
        sender.send("hello world!");
    }

}
