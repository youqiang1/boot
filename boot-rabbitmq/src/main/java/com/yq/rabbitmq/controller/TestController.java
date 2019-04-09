package com.yq.rabbitmq.controller;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.kernel.constants.RabbitmqConstants;
import com.yq.rabbitmq.entity.User;
import com.yq.rabbitmq.mq.sender.CallBackSender;
import com.yq.rabbitmq.mq.sender.FanoutSender;
import com.yq.rabbitmq.mq.sender.Sender;
import com.yq.rabbitmq.mq.sender.TopicSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试</p>
 * @author youq  2019/4/9 17:43
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private Sender sender;

    @Autowired
    private TopicSender topicSender;

    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private CallBackSender callBackSender;

    @GetMapping("/send1")
    public String send1(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        sender.send(RabbitmqConstants.QUEUE_STRING, user);
        return GlobalConstants.SUCCESS;
    }

    @GetMapping("/send2")
    public String send2(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        sender.sendUser(user);
        return GlobalConstants.SUCCESS;
    }

    /**
     * <p> topic 模式</p>
     * @author youq  2019/4/9 19:26
     */
    @GetMapping("/topicSend")
    public String topicSend() {
        topicSender.send("I am topic A message");
        return GlobalConstants.SUCCESS;
    }

    /**
     * <p> topic 模式</p>
     * @author youq  2019/4/9 19:26
     */
    @GetMapping("/topicSendB")
    public String topicSendB() {
        topicSender.sendB("I am topic B message");
        return GlobalConstants.SUCCESS;
    }

    /**
     * <p> fanout 模式</p>
     * @author youq  2019/4/9 19:26
     */
    @GetMapping("/fanoutSend")
    public String fanoutSend() {
        fanoutSender.send("I am fanout message");
        return GlobalConstants.SUCCESS;
    }

    @GetMapping("/callbackSend")
    public String callbackSend() {
        callBackSender.send("I am callback message");
        return GlobalConstants.SUCCESS;
    }

}
