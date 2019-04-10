package com.yq.kafka.controller;

import com.yq.kafka.proto.user.UserMessage;
import com.yq.kafka.service.UserService;
import com.yq.kernel.constants.GlobalConstants;
import com.yq.kernel.utils.IdGen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * <p> 用户controller</p>
 * @author youq  2019/4/10 11:02
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * <p> kafka消息生产</p>
     * @author youq  2019/4/10 11:04
     */
    @GetMapping("/kafkaProducer")
    public String kafkaProducer() {
        Random random = new Random();
        UserMessage.user userMessage = UserMessage.user.newBuilder()
                .setMethod("SEND")
                .setUniqueIden(IdGen.DEFAULT.gen())
                .setSend(
                        UserMessage.user.Send.newBuilder()
                                .setId(random.nextInt())
                                .setUsername("youq")
                                .setPassword("123445")
                                .setAge(28)
                                .setSex(UserMessage.user.Sex.male)
                                .setPhone("123456")
                                .setEmail("123@qwe.com")
                                .build())
                .build();
        userService.send(userMessage);
        return GlobalConstants.SUCCESS;
    }

}
